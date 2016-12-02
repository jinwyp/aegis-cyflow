package com.yimei.cflow.core

import java.util.Date

import akka.actor.{Actor, ActorLogging, ActorRef, PoisonPill, ReceiveTimeout}
import akka.cluster.sharding.ShardRegion.Passivate
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

object Flow {

  // 数据点: 值, 说明, 谁采集, 采集时间
  case class DataPoint(value: Int, memo: String, operator: String, timestamp: Date)

  // 接收命令
  case class CommandPoint(flowId: String, name: String, point: DataPoint)

  case class CommandPoints(flowId: String, points: Map[String, DataPoint])

  case class CommandQuery(flowId: String)

  case class CommandSnapshot(flowId: String)

  // 查询流程

  // persistent事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event
  case class PointsUpdated(pionts: Map[String, DataPoint]) extends Event

  case class DecisionUpdated(decision: Decision) extends Event

  // 状态
  case class State(userId: String, flowId: String, points: Map[String, DataPoint], decision: Decision, histories: List[Decision])

  // 分支边
  trait Edge {
    def schedule(self: ActorRef, state: State): Unit

    // 发起哪些数据采集
    def check(state: State): Boolean

    // 如何判断edge完成
    def description = "todo edge" // 分支描述
  }

  trait Decision {
    def run(state: State): Decision
  }

  trait Decided extends Decision

  case object FlowSuccess extends Decided {
    def run(state: State) = FlowSuccess

    override def toString = "Success"
  }

  case object FlowFail extends Decided {
    def run(state: State) = FlowFail

    override def toString = "Fail"
  }

  case object FlowTodo extends Decided {
    def run(state: State) = FlowTodo

    override def toString = "Todo"
  }

  abstract class Judge extends Decision {
    // 如edge
    def in: Edge

    // 计算结果
    def run(state: State): Decision = {
      if (!in.check(state)) {
        FlowTodo
      } else {
        decide(state)
      }
    }

    // 依据状态评估分支: 成功, 失败, 或者继续评估
    def decide(state: State): Decision
  }

}

abstract class AbstractFlow {

  import Flow._

  // 数据点名称 -> 数据点值
  var state: State

  //
  def queryStatus: String

  //
  def updateState(ev: Event) = {
    ev match {
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case PointsUpdated(map) => state = state.copy(points = state.points ++ map)
      case DecisionUpdated(d) => state = state.copy(decision = d, histories = state.decision :: state.histories)
    }
  }
}



// 抽象流程
abstract class Flow extends AbstractFlow with Actor with ActorLogging {

  import Flow._

  // 一般actor
  def receive = {
    case cmdpoint: CommandPoint =>
      log.info(s"收到$cmdpoint")
      processCommandPoint(cmdpoint)

    case cmdpoints: CommandPoints =>
      log.info(s"收到$cmdpoints")
      processCommandPoints(cmdpoints)

    case query: CommandQuery =>
      log.info("收到CommandQuery")
      sender() ! queryStatus
  }

  // 处理命令
  protected def processCommandPoint(cmd: CommandPoint) = {
    // 更新状体
    updateState(PointUpdated(cmd.name, cmd.point))

    // 决策
    val decidor = state.decision.run(state)
    decidor match {
      case j: Judge =>
        updateState(DecisionUpdated(decidor))
        log.info(s"userId[${state.userId}] => 开始决策...")
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: $j\n\n")
        j.in.schedule(self, state)
      case FlowTodo =>
      case FlowSuccess =>
        updateState(DecisionUpdated(decidor))
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: 成功")
      case FlowFail =>
        updateState(DecisionUpdated(decidor))
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: 失败")
    }
  }

  // 处理复合命令
  protected def processCommandPoints(cmdpoints: CommandPoints) = {

    // 更新所有状态
    for ((name, point) <- cmdpoints.points) {
      updateState(PointUpdated(name, point))
    }

    // 决策
    state.decision.run(state) match {
      case j: Judge =>
        updateState(DecisionUpdated(j))
        j.in.schedule(self, state)
      case a =>
        println(a)
    }
  }
}

abstract class PersistentFlow extends AbstractFlow with PersistentActor with ActorLogging {

  import Flow._
  // 恢复
  def receiveRecover = {
    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)
    case SnapshotOffer(_, snapshot: State) =>
      log.info(s"recover with snapshot: $snapshot")
      state = snapshot
    case RecoveryCompleted =>
      log.info(s"recover completed")
      log.info(s"current state: $state\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!恢复决策!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
      if (state.decision == FlowSuccess || state.decision == FlowFail) {
      } else {
        makeDecision
      }
  }

  // 命令
  def receiveCommand = {
    case cmd: CommandPoint =>
      log.info(s"收到$cmd")
      processCommandPoint(cmd)

    case cmds: CommandPoints =>
      log.info(s"收到$cmds")
      processCommandPoints(cmds)

    case query: CommandQuery =>
      log.info("收到CommandQuery")
      sender() ! queryStatus

    case snapshot: CommandSnapshot =>
      log.info("收到CommandSnap")
      saveSnapshot(state)

  }

  override def unhandled(msg: Any): Unit = msg match {
    case ReceiveTimeout =>
      log.info(s"${persistenceId}超时, 开始钝化!!!!")
      context.parent ! Passivate(stopMessage = PoisonPill)
    case _ =>
      log.error(s"收到unhandled消息 = $msg")
      super.unhandled(msg)
  }

  // 处理命令
  protected def processCommandPoint(cmd: CommandPoint) = {

    persist(PointUpdated(cmd.name, cmd.point)) { event =>

      // 更新状体
      log.info(s"持久化${event}成功")
      updateState(event)

      // 决策
      val decidor = state.decision.run(state)

      if (decidor != FlowTodo) {
        persist(DecisionUpdated(decidor)) { event =>
          log.info(s"持久化${event}成功")
          updateState(event)
        }
      } else {

      }
      makeDecision  // 作决定!!!
    }
  }

  // 处理复合命令
  protected def processCommandPoints(cmds: CommandPoints) = {
    persist(PointsUpdated(cmds.points)) { event =>
      log.info(s"持久化${event}成功")
      updateState(event)
    }
    makeDecision // 作决定
  }

  protected def makeDecision: Unit = {
    state.decision.run(state) match {
      case j: Judge =>
        log.info(s"userId[${state.userId}] => 开始决策...")
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策[${state.decision}]结果为: $j\n\n")
        j.in.schedule(self, state)
      case FlowSuccess =>
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策[${state.decision}]结果为: 成功")
        persist(DecisionUpdated(FlowSuccess)) { event =>
          log.info(s"持久化${event}成功")
          updateState(event)
        }
      case FlowFail =>
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策[${state.decision}]结果为: 失败")
        persist(DecisionUpdated(FlowFail)) { event =>
          log.info(s"持久化${event}失败")
          updateState(event)
        }
      case FlowTodo =>
        log.info(s"userId[${state.userId}] -> 当前决策[${state.decision}]结果为: FlowTodo")
    }
  }
}

