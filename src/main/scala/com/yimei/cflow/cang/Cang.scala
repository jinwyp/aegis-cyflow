package com.yimei.cflow.cang

import akka.actor.{ActorLogging, Props}
import com.yimei.cflow.cang.CangGraph.V1
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow.{DataPoint, State}

object Cang {
  def props(flowId: String) = Props(new Cang(flowId))
}

class Cang(flowId: String) extends Flow with ActorLogging {
  override var state = State(flowId, Map[String, DataPoint](), V1, Nil)
  override def queryStatus = CangGraph.cangJsonGraph(state)
}

