package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.cang.CangGraph.V1
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow.{DataPoint, State}

object Ying {
  def props(userId: String, orderId: String) = Props(new Ying(userId, orderId))

  // 请求创建仓押流程
  case class CreateFlowRequest(userId: String)
  case class CreateFlowResponse(orderId: String, flowRef: ActorRef)
}

class Ying(userId: String, orderId: String) extends Flow with ActorLogging {
  override var state = State(userId, orderId, Map[String, DataPoint](), V1, Nil)
  override def queryStatus = YingGraph.cangJsonGraph(state)
}

