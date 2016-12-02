package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.core.Flow.{DataPoint, State}
import com.yimei.cflow.core.PersistentFlow
import com.yimei.cflow.ying.YingGraph.V1

object Ying {
  def props(flowId: String) = Props(new Ying(flowId))
}

// 10秒钝化
class Ying(flowId: String) extends PersistentFlow(10) with ActorLogging {

  override val persistenceId = flowId

  override var state = State(flowId, Map[String, DataPoint](), V1, Nil)

  override def queryStatus = YingGraph.yingJsonGraph(state)
}

