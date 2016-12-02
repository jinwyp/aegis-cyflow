package com.yimei.cflow.core

import java.util.Date

import akka.actor.{ActorLogging, ActorRef, PoisonPill, ReceiveTimeout}
import akka.cluster.sharding.ShardRegion
import akka.cluster.sharding.ShardRegion.Passivate
import akka.persistence.{PersistentActor, SnapshotOffer}


object PersistentFlow {

  // 数据点: 值, 说明, 谁采集, 采集时间
  case class DataPoint(name: String, value: Int, memo: String, operator: String, timestamp: Date)

  // 单个数据点
  // 批量数据点
  // 查询流程
  // 保存snapshot
  case class Command(orderId: String, point: DataPoint)
  case class CommandSeq(orderId: String, commands: Array[DataPoint])
  case class CommandQuery(orderId: String)
  case class CommandSnap(orderId: String)

  // persistent事件
  trait Event
  case class PointUpdated(point: DataPoint) extends Event
  case class DecisionUpdated(decision: Decision) extends Event

  // 状态
  case class State(userId: String, orderId: String, points: Map[String, DataPoint], decision: Decision, histories: List[Decision])

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

  // 从命令中获取实体cmd =>  id + cmd
  val idExtractor: ShardRegion.ExtractEntityId = {
    case cmd: Command => ("abc", cmd)
    case cmds: CommandSeq =>
    case cmdQuery: CommandQuery =>
    case cmdSnap: CommandSnap =>
  }

  // cmd => shard-id
  val shardResolver: ShardRegion.ExtractShardId = {
    case cmd: Command => (math.abs(1.hashCode) % 100).toString
    case cmds: CommandSeq =>
  }

  val shardName = "myShard"
}

// 抽象流程
abstract class PersistentFlow extends PersistentActor with ActorLogging {

  import PersistentFlow._

  import scala.concurrent.duration._

  // 数据点名称 -> 数据点值
  var state: State

  // 超时钝化
  context.setReceiveTimeout(15 seconds)

  // 查询当前状态
  def queryStatus: String

  //
  def updateState(ev: Event) = {
    ev match {
      case PointUpdated(name, point) =>
        state = state.copy(points = state.points + (name -> point))
      case DecisionUpdated(d) =>
        state = state.copy(decision = d, histories = state.decision :: state.histories)
    }
  }

  // 恢复
  def receiveRecover = {
    case ev: Event => updateState(ev)
    case SnapshotOffer(_, snapshot: State) => state = snapshot
  }

  // 命令
  def receiveCommand = {
    case cmd: Command =>
      log.info(s"收到$cmd")
      processCommand(cmd)

    case cmds: CommandSeq =>
      log.info(s"收到$cmds")
      processCommandSeq(cmds)

    case CommandQuery =>
      log.info("收到CommandQuery")
      sender() ! queryStatus

    case CommandSnap =>
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
  protected def processCommand(cmd: Command) = {

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
  protected def processCommandSeq(cmds: CommandSeq) = {

    // 更新所有状态
    cmds.commands.foreach(
      x => persist(PointUpdated(x.name, x.point)) { event =>
        log.info(s"持久化${event}成功")
        updateState(event)
      }
    )
    makeDecision // 作决定
  }

  private def makeDecision = {
      state.decision.run(state) match {
      case j: Judge =>
        log.info(s"userId[${state.userId}] => 开始决策...")
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: $j\n\n")
        j.in.schedule(self, state)
      case FlowSuccess =>
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: 成功")
      case FlowFail =>
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: 失败")
    }
  }
}

