package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow.{DataPoint, State, VoidEdge}
import com.yimei.cflow.core.{GraphBuilder, PersistentFlow}
import com.yimei.cflow.ying.YingGraph._

object Ying extends Core {
  def props(flowId: String, modules: Map[String, ActorRef]) = Props(new Ying(flowId, modules, config.getInt("flow.ying.timeout")))
}

class Ying(flowId: String, modules: Map[String, ActorRef], timeout: Int) extends PersistentFlow(modules, timeout) with ActorLogging {

  override val persistenceId = flowId

  override var state = State(flowId, Map[String, DataPoint](), V0, Nil)

  import GraphBuilder._

  // 查询图
  override def queryStatus =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      V1 ~> E1 ~> V2
      V2 ~> E2 ~> V3
      V3 ~> E3 ~> V4
      V4 ~> E4 ~> V5
      V5 ~> E5 ~> V6

      V3 ~> VoidEdge ~> V7
      builder
    }
}

