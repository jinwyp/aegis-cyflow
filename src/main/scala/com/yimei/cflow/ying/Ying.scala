package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.core.Flow.{DataPoint, State}
import com.yimei.cflow.core.PersistentFlow
import com.yimei.cflow.ying.YingGraph.V1

object Ying {
  def props(userId: String, flowId: String) = Props(new Ying(userId, flowId))

  // 请求创建应收流程
  case class CreateFlowRequest(userId: String)
  case class CreateFlowResponse(flowId: String, flowRef: ActorRef)

}

class Ying(userId: String, flowId: String) extends PersistentFlow with ActorLogging {

  override val persistenceId = flowId

  override var state = State(userId, flowId, Map[String, DataPoint](), V1, Nil)

  override def queryStatus = YingGraph.yingJsonGraph(state)
}

