package com.yimei.cflow.core

import java.util.Date

import akka.actor.{Actor, ActorLogging, ActorRef, Props, ReceiveTimeout, SupervisorStrategy, Terminated}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.core.Flow.{Command, StartFlow}

object Flow {

  // 启动流程
  case class StartFlow(flowId: String)

  // 数据点: 值, 说明, 谁采集, 采集时间
  case class DataPoint(value: Int, memo: String, operator: String, timestamp: Date)

  // 接收命令
  trait Command { def flowId: String }

  case object CommandInitiation  // 流程启动

  case class CommandPoint(flowId: String, name: String, point: DataPoint) extends Command

  case class CommandPoints(flowId: String, points: Map[String, DataPoint]) extends Command

  case class CommandQuery(flowId: String) extends Command

  case class CommandSnapshot(flowId: String) extends Command

  case class CommandShutdown(flowId: String) extends Command

  // 查询流程

  // persistent事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event

  case class PointsUpdated(pionts: Map[String, DataPoint]) extends Event

  case class DecisionUpdated(decision: Decision) extends Event

  // 状态
  case class State(flowId: String, points: Map[String, DataPoint], decision: Decision, histories: List[String])

  // 分支边
  trait Edge {
    def schedule(self: ActorRef, state: State): Unit

    // 发起哪些数据采集
    def check(state: State): Boolean

    // 如何判断edge完成
    def description = "todo edge" // 分支描述
  }

  // 初始变是用来填充用户信息的
  object InitialEdge extends Edge {
    def schedule(self: ActorRef, state: State) = self ! Flow.CommandInitiation;
    def check(state: State): Boolean = true   // 自启动, 当然为true
    override def description = "initial edge" // 分支描述
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
      case DecisionUpdated(d) => state = state.copy(decision = d, histories = state.decision.toString :: state.histories)
    }
  }
}


// 抽象流程
abstract class Flow extends AbstractFlow with Actor with ActorLogging {

  import Flow._

  // 一般actor
  def receive = {
    // 启动流程
    case CommandInitiation =>
      log.info(s"收到CommandInitial, 启动流程")
      state.decision.run(state).asInstanceOf[Judge].in.schedule(self, state)

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
        log.info(s"开始决策...")
        log.info(s"当前状态为: $state")
        log.info(s"当前决策结果为: $j")
        j.in.schedule(self, state)
      case FlowTodo =>
      case FlowSuccess =>
        updateState(DecisionUpdated(decidor))
        log.info(s"当前状态为: $state")
        log.info(s"当前决策结果为: FlowSuccess")
      case FlowFail =>
        updateState(DecisionUpdated(decidor))
        log.info(s"当前状态为: $state")
        log.info(s"当前决策结果为: FlowFail")
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

abstract class PersistentFlow(passivateTimeout: Long) extends AbstractFlow with PersistentActor with ActorLogging {

  import Flow._

  import concurrent.duration._

  context.setReceiveTimeout(passivateTimeout seconds)

  // 钝化超时时间

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
      log.info(s"current state: $state")
      if (state.decision.toString == "Success" || state.decision.toString == "Fail") {
      } else {
        log.info("恢复决策")
        makeDecision
      }
  }

  // 命令处理
  def receiveCommand = {
    // 流程自启动
    case CommandInitiation =>
      log.info(s"收到CommandInitial, 启动流程")
      state.decision.run(state).asInstanceOf[Judge].in.schedule(self, state)

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
      log.info("收到CommandSnapshot")
      saveSnapshot(state)

    case shutdown: CommandShutdown =>
      log.info("收到CommandShutdown")
      context.stop(self)

    // 收到超时
    case ReceiveTimeout =>
      log.info(s"${persistenceId}超时, 开始钝化!!!!")
      context.stop(self)

    case _ =>
  }

  override def unhandled(msg: Any): Unit = log.error(s"收到unhandle消息: $msg")

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
      makeDecision // 作决定!!!
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

  protected def makeDecision(): Unit = {
    state.decision.run(state) match {
      case j: Judge =>
        log.info(s"> 开始决策...")
        log.info(s"> 当前状态为: $state")
        log.info(s"> 当前决策[${state.decision}]结果为: $j")
        j.in.schedule(self, state)
      case FlowSuccess =>
        log.info(s"> 当前状态为: $state")
        log.info(s"> 当前决策[${state.decision}]结果为: FlowSuccess")
        persist(DecisionUpdated(FlowSuccess)) { event =>
          log.info(s"持久化${event}成功")
          updateState(event)
          self ! CommandSnapshot(persistenceId)  // snapshot
        }
      case FlowFail =>
        log.info(s"> 当前状态为: $state")
        log.info(s"> 当前决策[${state.decision}]结果为: FlowFail")
        persist(DecisionUpdated(FlowFail)) { event =>
          log.info(s"持久化${event}失败")
          updateState(event)
          self ! CommandSnapshot(persistenceId)  // snapshot
        }
      case FlowTodo =>
        log.info(s"> 当前决策[${state.decision}]结果为: FlowTodo")
    }
  }
}

trait Supervisor extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  def receive = {
    case StartFlow(flowId) =>
      context.child(flowId).fold(create(flowId))(identity)

    case command: Command =>
      val child = context.child(command.flowId).fold(create(command.flowId))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  def create(flowId: String) = {
    context.actorOf(flowProp(flowId), flowId)
  }

  def flowProp(flowId: String): Props
}

