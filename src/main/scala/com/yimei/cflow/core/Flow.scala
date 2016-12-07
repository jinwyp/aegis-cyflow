package com.yimei.cflow.core

import java.util.Date

import akka.actor.ActorRef
import com.yimei.cflow.integration.DependentModule

object Flow {

  case class FlowGraphJson(json: String)

  // 数据点: 值, 说明, 谁采集, 采集id, 采集时间
  case class DataPoint(value: Int, memo: String, operator: String, id: String, timestamp: Date)

  // create flow, but not run it
  case class CommandCreateFlow(flowType: String, userId: String, parties: Map[String, String] = Map())


  // response of CommandCreateFlow
  case class CreateFlowSuccess(flowId: String)
  case class RunFlowSuccess(flowId: String)

  case object ShutDownSuccess

  // shutdown the flow

  // 接收命令
  trait Command {
    def flowId: String // flowType-userId-uuid
  }

  // 启动流程
  case class CommandRunFlow(flowId: String) extends Command

  // 停止流程
  case class CommandShutdown(flowId: String) extends Command

  // 收到数据点
  case class CommandPoint(flowId: String, name: String, point: DataPoint) extends Command

  // 收到数据点集合
  case class CommandPoints(flowId: String, points: Map[String, DataPoint]) extends Command

  // 查询流程
  case class CommandQueryFlow(flowId: String) extends Command


  // persistent事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event

  case class PointsUpdated(pionts: Map[String, DataPoint]) extends Event

  case class DecisionUpdated(decision: Decision) extends Event

  // 状态
  case class State(flowId: String, userId: String, parties: Map[String, String], points: Map[String, DataPoint], decision: Decision, histories: List[String])

  // 分支边
  trait Edge {
    /**
      * @param state   流程状态
      * @param modules 这个流程依赖的模块
      */
    def schedule(state: State, modules: Map[String, ActorRef]): Unit

    // 发起哪些数据采集
    def check(state: State): Boolean

    // 如何判断edge完成
    override def toString = "todo edge" // 分支描述
  }

  // 直通边
  object VoidEdge extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef] = Map()) =
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

    override def toString = "FlowSuccess"
  }

  case object FlowFail extends Decided {
    def run(state: State) = FlowFail

    override def toString = "FlowFail"
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

    // 依据状态评估分支: success, 失败, 或者继续评估
    def decide(state: State): Decision
  }

}

// 抽象流程
abstract class Flow extends AbstractFlow with DependentModule {

  import Flow._

  override def receive: Receive = commonBehavior orElse serving

  val serving: Receive = {
    case cmd@CommandRunFlow(flowId) =>
      log.info(s"收到${cmd}")
      makeDecision() // 注意顺序

    case cmd: CommandPoint =>
      log.info(s"received ${cmd.name}")
      processCommandPoint(cmd)

    case cmd: CommandPoints =>
      log.info(s"received ${cmd.points.map(_._1).mkString("[", ",", "]")}")
      processCommandPoints(cmd)
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

        // 尝试再次计算, 因为有可能已经满足条件了
        if (j.in.check(state)) {
          makeDecision()
        } else {
          j.in.schedule(state, modules)
        }
      case FlowTodo =>
        logState("FlowTodo")
      case FlowSuccess =>
        updateState(DecisionUpdated(decidor))
        logState("FlowSuccess")
      case FlowFail =>
        updateState(DecisionUpdated(decidor))
        logState("FlowFail")
    }
  }
}


