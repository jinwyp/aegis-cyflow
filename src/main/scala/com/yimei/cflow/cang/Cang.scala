package com.yimei.cflow.cang

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.Flow
import com.yimei.cflow.Flow._
import com.yimei.cflow.cang.CangGraph.V1

object Cang {
  def props(userId: String, orderId: String) = Props(new Cang(userId, orderId))

  // 请求创建仓押流程
  case class CreateFlowRequest(userId: String)
  case class CreateFlowResponse(orderId: String, flowRef: ActorRef)
}

class Cang(userId: String, orderId: String) extends Flow with ActorLogging {
  override var state = State(userId, orderId, Map[String, DataPoint](), V1, Nil)
  override def queryStatus = CangGraph.cangJsonGraph(state)
}

