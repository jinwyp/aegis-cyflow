package com.yimei.cflow.engine.flow

import akka.actor.ActorRef
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.api.models.flow.{CommandCreateFlow, CommandFlowGraph, CommandFlowState, CommandHijack, CommandUpdatePoints, DataPoint, Graph, Command => FlowCommand, State => FlowState}

import scala.concurrent.Future

/**
  * Created by hary on 17/1/6.
  */
trait FlowService {

  def proxy: ActorRef

  def flowServiceTimeout: Timeout

  // 1> 创建流程 - 自动运行
  // 2> 查询流程
  // 3> 管理员更新数据点

  def flowCreate(userType: String, userId: String, flowType: String, init: Map[String, String] = Map()) =
    (proxy ? CommandCreateFlow(flowType, s"${userType}!${userId}", init))(flowServiceTimeout).mapTo[FlowState]

  def flowGraph(flowId: String) =
    (proxy ? CommandFlowGraph(flowId))(flowServiceTimeout).mapTo[Graph]

  def flowState(flowId: String) =
    (proxy ? CommandFlowState(flowId))(flowServiceTimeout).mapTo[FlowState]

  def flowUpdatePoints(flowId: String, updatePoint: Map[String, String], trigger: Boolean): Future[FlowState] =
    (proxy ? CommandUpdatePoints(flowId, updatePoint, false))(flowServiceTimeout).mapTo[FlowState] // todo

  def flowHijack(flowId: String, updatePoints: Map[String, DataPoint], decision: Option[String], trigger: Boolean): Future[FlowState] =
    (proxy ? CommandHijack(flowId, updatePoints, decision, trigger))(flowServiceTimeout).mapTo[FlowState]
}
