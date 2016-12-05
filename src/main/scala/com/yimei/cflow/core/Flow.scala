package com.yimei.cflow.core

import java.util.Date

import akka.actor.{Actor, ActorLogging, ActorRef, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

import scala.concurrent.duration._

object Flow {

  case class FlowGraphJson(json: String)

  // 数据点: 值, 说明, 谁采集, 采集id, 采集时间
  case class DataPoint(value: Int, memo: String, operator: String, id: String, timestamp: Date)

  // 这两个命令式给flowmaster用的
  case class  CommandCreateFlow(flowId: String, userId: String)
  case object CreateSuccess
  case object CommandRunFlow
  case class  CommandShutdown(flowId: String) extends Command

  // 接收命令
  trait Command {
    def flowId: String
  }

  // 收到采集结果,  flowId是用来分片的
  case class CommandPoint(flowId: String, name: String, point: DataPoint) extends Command

  // 收到采集结果,  flowId是用来分片的
  case class CommandPoints(flowId: String, points: Map[String, DataPoint]) extends Command

  case class CommandQuery(flowId: String) extends Command


  // persistent事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event

  case class PointsUpdated(pionts: Map[String, DataPoint]) extends Event

  case class DecisionUpdated(decision: Decision) extends Event

  case class UserUpdated(userid: String) extends Event

  // 状态
  case class State(flowId: String, userId: String, parties: Map[String, String], points: Map[String, DataPoint], decision: Decision, histories: List[String])

  // 分支边
  trait Edge {
    /**
      * @param self    流程actor
      * @param state   流程状态
      * @param modules 这个流程依赖的模块
      */
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]): Unit

    // 发起哪些数据采集
    def check(state: State): Boolean

    // 如何判断edge完成
    override def toString = "todo edge" // 分支描述
  }

  // 直通边
  object VoidEdge extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) =
      throw new IllegalArgumentException("VoidEdge can not be scheduled")

    def check(state: State) = true

    override def toString = "void edge"
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

abstract class AbstractFlow(modules: Map[String, ActorRef]) extends Actor with ActorLogging {

  import Flow._

  // 数据点名称 -> 数据点值
  var state: State

  //
  def queryStatus: String

  //
  def updateState(ev: Event) = {
    ev match {
      case UserUpdated(newUserId) => state = state.copy(userId = newUserId)
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case PointsUpdated(map) => state = state.copy(points = state.points ++ map)
      case DecisionUpdated(d) => state = state.copy(decision = d, histories = state.decision.toString :: state.histories)
    }
  }

  def logState(mark: String = ""): Unit = {
    log.info(s"<$mark>当前状态为: {${state.decision} [${state.histories.mkString(",")}]} + {${state.points.map(_._1).mkString(",")}} + {${state.userId}}")
  }
}


// 抽象流程
abstract class Flow(modules: Map[String, ActorRef]) extends AbstractFlow(modules) {

  import Flow._

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    log.info("class Flow preStart")
    makeDecision() // 注意顺序
    super.preStart()
  }


  def receive = {
    case cmdpoint: CommandPoint =>
      log.info(s"收到${cmdpoint.name}")
      processCommandPoint(cmdpoint)

    case cmdpoints: CommandPoints =>
      log.info(s"收到${cmdpoints.points.map(_._1).mkString("[", ",", "]")}")
      processCommandPoints(cmdpoints)

    case query: CommandQuery =>
      log.info("收到CommandQuery")
      sender() ! queryStatus
  }

  // 处理命令
  protected def processCommandPoint(cmd: CommandPoint) = {
    // 更新状体
    updateState(PointUpdated(cmd.name, cmd.point))
    makeDecision()
  }

  // 处理复合命令
  protected def processCommandPoints(cmdpoints: CommandPoints) = {
    // 更新所有状态
    updateState(PointsUpdated(cmdpoints.points))
    makeDecision()
  }

  private def makeDecision() {
    // 决策
    val decidor = state.decision.run(state)
    decidor match {
      case j: Judge =>
        updateState(DecisionUpdated(decidor))
        log.info(s"调度 = ${j.in}")
        if (j.in.check(state)) {
          // 尝试再次计算, 因为有可能已经满足条件了
          makeDecision()
        } else {
          j.in.schedule(self, state, modules)
        }
      case FlowTodo =>
      case FlowSuccess =>
        updateState(DecisionUpdated(decidor))
        logState("FlowSuccess")
      case FlowFail =>
        updateState(DecisionUpdated(decidor))
        logState("FlowFail")
    }
  }
}

abstract class PersistentFlow(modules: Map[String, ActorRef], passivateTimeout: Long) extends AbstractFlow(modules)
  with PersistentActor {

  import Flow._

  // 钝化超时时间
  context.setReceiveTimeout(passivateTimeout seconds)

  // 恢复
  def receiveRecover = {

    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)
    case SnapshotOffer(_, snapshot: State) =>
      state = snapshot
      log.info(s"snapshot recovered")
    case RecoveryCompleted =>
      logState("recovery completed")

  }

  // 命令处理
  def receiveCommand = {
    case k : CommandRunFlow.type =>
      log.info(s"收到$k")
      val src = sender()
      persist(UserUpdated(state.userId)) { event =>
        updateState(event)
        src ! CreateSuccess
        log.info(s"持久化${event}成功")
        makeDecision
      }

    case cmd: CommandPoint =>
      log.info(s"收到${cmd.name}")
      processCommandPoint(cmd)

    case cmds: CommandPoints =>
      log.info(s"收到${cmds.points.map(_._1)}")
      processCommandPoints(cmds)

    case query: CommandQuery =>
      log.info("收到CommandQuery")
      sender() ! FlowGraphJson(queryStatus)

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

    persist(PointUpdated(cmd.name, cmd.point)) {
      event =>

        // 更新状体
        log.info(s"持久化${
          event.name
        }成功")
        updateState(event)

        // 作决定!!!
        makeDecision
    }
  }

  // 处理复合命令
  protected def processCommandPoints(cmds: CommandPoints) = {
    persist(PointsUpdated(cmds.points)) {
      event =>
        log.info(s"持久化${event.pionts.map(_._1)}成功")
        updateState(event)
        makeDecision // 作决定
    }
  }

  protected def makeDecision(): Unit = {
    state.decision.run(state) match {
      case j: Judge =>
        logState("begin judge")
        persist(DecisionUpdated(j)) {
          event =>
            log.info(s"持久化${ event.decision }成功")
            updateState(event)
            log.info(s"调度 = ${ j.in }")
            if (j.in.check(state)) {
              // 继续调度下一个节点,  maybe, 下一个节点不需要采集新的要素
              makeDecision()
            } else {
              j.in.schedule(self, state, modules)
            }
        }
      case FlowSuccess =>
        logState("FlowSuccess")
        persist(DecisionUpdated(FlowSuccess)) {
          event =>
            log.info(s"持久化${ event.decision }成功, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case FlowFail =>
        logState("FlowFail")
        persist(DecisionUpdated(FlowFail)) {
          event =>
            log.info(s"持久化${ event.decision }成功, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case FlowTodo =>
        log.info(s"当前决策[${ state.decision }]结果为: FlowTodo")
    }
  }
}
