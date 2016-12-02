package com.yimei.cflow.persist

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.core.PersistentFlow
import com.yimei.cflow.core.PersistentFlow.{DataPoint, State}
import com.yimei.cflow.persist.PersistGraph.V1

object Persist {
  def props() = Props(new Persist())
  case class CreateFlowRequest(userId: String)
  case class CreateFlowResponse(orderId: String, flowRef: ActorRef)
}

class Persist extends PersistentFlow with ActorLogging {

  override def persistenceId: String = self.path.parent.name + "-" + self.path.name

  override var state = State(null, null, Map[String, DataPoint](), V1, Nil)

  override def queryStatus = PersistGraph.persistJsonGraph(state)
}

