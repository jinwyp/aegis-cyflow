package com.yimei.cflow.core

import java.text.SimpleDateFormat
import java.util.Date

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.yimei.cflow.core.Flow.{DataPoint, Decision, Edge, State}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}




object Flow {
  // 数据点: 值, 说明, 谁采集, 采集时间
  case class DataPoint(value: Int, memo: String, operator: String, timestamp: Date)

  // 接收消息
  case class Command(name: String, point: DataPoint)
  case class CommandSeq(commands: Array[Command])
  case object CommandQuery  // 查询流程

  // persistent事件
  trait Event
  case class PointUpdated(name: String, point: DataPoint) extends Event
  case class DecisionUpdated(decision: Decision) extends Event

  // 状态
  case class State(
    userId: String,
    orderId: String,
    points: Map[String, DataPoint],
    decision: Decision,
    histories: List[Decision])

  // 分支边
  trait Edge {
    def schedule(self: ActorRef, state: State): Unit // 发起哪些数据采集
    def check(state: State): Boolean // 如何判断edge完成
    def description = "todo edge"    // 分支描述
  }

  trait Decision {
    def run(state: State): Decision
  }
  trait Decided extends Decision
  case object FlowSuccess extends Decided {
    def run(state: State) = FlowSuccess
    override  def toString = "Success"
  }
  case object FlowFail extends Decided {
    def run(state: State) = FlowFail
    override  def toString = "Fail"
  }
  case object FlowTodo extends Decided {
    def run(state: State) = FlowTodo
    override  def toString = "Todo"
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
    def decide(state: State):Decision
  }
}

// 抽象流程
abstract class Flow extends Actor with ActorLogging{

  import Flow._

  // 数据点名称 -> 数据点值
  var state: State

  //
  def queryStatus: String

  //
  def update(ev: Event) = {
    ev match {
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case DecisionUpdated(d) => state = state.copy(decision = d, histories = state.decision::state.histories)
    }
  }

  // 持久化actor 接收命令
  //  def recieveCommand = {
  //	case Command(name, data) =>
  //        persist(PointUpdated(name, data)){ ev =>
  //		  update(ev)
  //	      val decidor = state.decidor.run(state)
  //	      decidor match {
  //		    case j: Judge   =>  persist(DecisionUpdated(j)) { update(_) }
  //	      }
  //	   }
  //  }

  // 一般actor
  def receive = {
    case cmd: Command =>
      log.info(s"收到$cmd")
      processCommand(cmd)

    case cmds: CommandSeq =>
      log.info(s"收到$cmds")
      processCommandSeq(cmds)

    case CommandQuery =>
      log.info("收到CommandQuery")
      sender() ! queryStatus
  }

  // 处理命令
  protected def processCommand(cmd: Command) = {
    // 更新状体
    update(PointUpdated(cmd.name, cmd.point))

    // 决策
    val decidor = state.decision.run(state)
    decidor match {
      case j: Judge =>
        update(DecisionUpdated(decidor))
        log.info(s"userId[${state.userId}] => 开始决策...")
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: $j\n\n")
        j.in.schedule(self, state)
      case FlowTodo =>
      case FlowSuccess =>
        update(DecisionUpdated(decidor))
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: 成功")
      case FlowFail =>
        update(DecisionUpdated(decidor))
        log.info(s"userId[${state.userId}] => 当前状态为: $state")
        log.info(s"userId[${state.userId}] => 当前决策结果为: 失败")
    }
  }

  // 处理复合命令
  protected def processCommandSeq(cmds: CommandSeq) = {

    // 更新所有状态
    cmds.commands.foreach(x => update(PointUpdated(x.name, x.point)));

    // 决策
    state.decision.run(state) match {
      case j: Judge =>
        update(DecisionUpdated(j))
        j.in.schedule(self, state)
      case a =>
        println(a)
    }
  }

}

