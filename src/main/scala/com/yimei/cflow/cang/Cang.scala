package com.yimei.cflow.cang

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.cang.CangGraph.V1
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow.{DataPoint, State}

object Cang {
  def props(userId: String, flowId: String) = Props(new Cang(userId, flowId))

  // 请求创建仓押流程
  case class CreateFlowRequest(userId: String)
  case class CreateFlowResponse(flowId: String, flowRef: ActorRef)
}

class Cang(userId: String, flowId: String) extends Flow with ActorLogging {
  override var state = State(userId, flowId, Map[String, DataPoint](), V1, Nil)
  override def queryStatus = CangGraph.cangJsonGraph(state)
}

