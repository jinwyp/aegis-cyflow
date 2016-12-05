package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow.{DataPoint, State, VoidEdge}
import com.yimei.cflow.core.{GraphBuilder, PersistentFlow}
import com.yimei.cflow.ying.YingGraph._

object Ying extends Core {
  def props(flowId: String, modules: Map[String, ActorRef],
            userId: String,
            parties: Map[String, String] = Map()
           ) =
    Props(new Ying(flowId, modules, userId, parties, config.getInt("flow.ying.timeout")))

  def postProps(flowId: String, modules: Map[String, ActorRef]) =
    Props(new Ying(flowId, modules, "", Map(), config.getInt("flow.ying.timeout")))

}

class Ying(flowId: String,
           modules: Map[String, ActorRef],
           userId: String,
           parties: Map[String, String],
           timeout: Int) extends PersistentFlow(modules, timeout) with ActorLogging {

  override val persistenceId = flowId

  override var state = State(flowId, userId, parties, Map[String, DataPoint](), V0, Nil)

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

