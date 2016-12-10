package com.yimei.cflow.core

import java.util.{Date, UUID}

import akka.actor.ActorRef
import com.yimei.cflow.integration.DependentModule

object Flow {

  // 数据点: 值, 说明, 谁采集, 采集id, 采集时间
  case class DataPoint(value: String, memo: Option[String], operator: Option[String], id: String, timestamp: Date)

  // create flow, but not run it
  case class CommandCreateFlow(flowType: String, guid: String, parties: Map[String, String] = Map())

  // response of CommandCreateFlow
  case class CreateFlowSuccess(flowId: String)
  case class RunFlowSuccess(flowId: String)

  // shutdown the flow

  // 接收命令
  trait Command {
    def flowId: String // flowType-userType-userId-uuid
  }

  // 启动流程
  case class CommandRunFlow(flowId: String) extends Command

  // 停止流程
  case class CommandShutdown(flowId: String) extends Command

  // 收到数据点
  case class CommandPoint(flowId: String, name: String, point: DataPoint) extends Command

  // 收到数据点集合  -- 同质数据点
  case class CommandPoints(flowId: String, points: Map[String, DataPoint]) extends Command

  // 查询流程
  case class CommandQueryFlow(flowId: String) extends Command

  // 手动更新points
  case class CommandUpdatePoints(flowId: String, points: Map[String, String])  extends Command

  // 更新流程关联方  todo 王琦
  case class CommandUpdateParties(flowId: String, parties: Map[String, String])  extends Command

  // persistent事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event

  case class PointsUpdated(pionts: Map[String, DataPoint]) extends Event

  case class DecisionUpdated(arrow: Arrow) extends Event

  case class PartiesUpdated(parties: Map[String, String]) extends Event

  // 状态
  case class State(
            flowId: String,
            guid: String,
            parties: Map[String, String],
            points: Map[String, DataPoint],
            decision: Decision,
            edge: Option[Edge],
            histories: List[Arrow])
  // 分支边
  trait Edge {
    /**
      * 调度采集数据
      * @param state   流程状态
      * @param modules 这个流程依赖的模块
      */
    def schedule(state: State, modules: Map[String, ActorRef]): Unit

    def check(state: State): Boolean

  }

  // 开始边
  case object EdgeStart extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef] = Map()) =
      throw new IllegalArgumentException("VoidEdge can not be scheduled")

    def check(state: State) = true

    override def toString = "Start"

//    def describe() = ???   // 描述这条边
  }

  trait Decision {
    def run(state: State): Arrow
//    def describe() = ??? // 描述这个决策点
  }

  trait Decided extends Decision

  case object FlowSuccess extends Decided {
    def run(state: State) = Arrow(FlowSuccess, None)

    override def toString = "FlowSuccess"
  }

  case object FlowFail extends Decided {
    def run(state: State) = Arrow(FlowFail, None)

    override def toString = "FlowFail"
  }

  case object FlowTodo extends Decided {
    def run(state: State) = Arrow(FlowTodo, None)

    override def toString = "FlowTodo"
  }

  abstract class Judge extends Decision {

    // 计算结果
    def run(state: State): Arrow = {
      state.edge match {
        case None =>
          throw new IllegalArgumentException("impossible here")
        case Some(e) =>
          if (!e.check(state)) {
            Arrow(FlowTodo, None)
          } else {
            decide(state)
          }
      }
    }

    // 依据状态评估分支: success, 失败, 或者继续评估
    def decide(state: State): Arrow
  }

  case class Graph(edges: Map[Edge, Array[Decision]], state: State, dataDescription: Map[String, String])

  case class Arrow(end: Decision, edge: Option[Edge])

}

// 抽象流程
abstract class Flow extends AbstractFlow with DependentModule {

  import Flow._

  override def receive: Receive = commonBehavior orElse serving

  val serving: Receive = {
    case cmd@CommandRunFlow(flowId) =>
      log.info(s"收到${cmd}")
      sender() ! RunFlowSuccess(flowId)
      makeDecision() // 注意顺序

    case cmd: CommandPoint =>
      log.info(s"received ${cmd.name}")
      processCommandPoint(cmd)

    case cmd: CommandPoints =>
      log.info(s"received ${cmd.points.map(_._1).mkString("[", ",", "]")}")
      processCommandPoints(cmd)

    case cmd: CommandUpdatePoints =>
      val uuid = UUID.randomUUID().toString
      val points: Map[String, DataPoint] = cmd.points.map { entry =>
        entry._1 -> DataPoint(entry._2, None, None, uuid, new Date())
      }
      updateState(PointsUpdated(points))

    case cmd: CommandUpdateParties =>
      updateState(PartiesUpdated(cmd.parties))
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
    val curDecision = state.decision
    val curEdge = state.edge

    curDecision.run(state) match {
      case arrow @ Arrow(j, Some(e)) =>
        updateState(DecisionUpdated(arrow))
        log.info(s"调度 = ${e}")

        // 继续执行图!!!
        if( e.check(state)) {
          makeDecision()
        } else {
          e.schedule(state, modules)
        }

      case Arrow(FlowTodo, None) =>
        logState("FlowTodo")

      case Arrow(FlowSuccess, None) =>
        updateState(DecisionUpdated(Arrow(FlowSuccess, None)))
        logState("FlowSuccess")

      case Arrow(FlowFail, None) =>
        updateState(DecisionUpdated(Arrow(FlowFail, None)))
        logState("FlowFail")
    }
  }
}

