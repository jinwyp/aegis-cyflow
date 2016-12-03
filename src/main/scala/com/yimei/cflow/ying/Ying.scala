package com.yimei.cflow.ying

import akka.actor.{ActorLogging, Props}
import com.yimei.cflow.core.Flow.{DataPoint, State}
import com.yimei.cflow.core.{GraphBuilder, PersistentFlow}
import com.yimei.cflow.ying.YingGraph._

object Ying {
  def props(flowId: String) = Props(new Ying(flowId))
}

// 10秒钝化
class Ying(flowId: String) extends PersistentFlow(10) with ActorLogging {

  override val persistenceId = flowId

  override var state = State(flowId, Map[String, DataPoint](), V0, Nil)

  import GraphBuilder._

  // 查询图
  override def queryStatus =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      V1 ~> E3 ~> V4
      V1 ~> E1 ~> V2
      V4 ~> E4 ~> V5
      V4 ~> E5 ~> V6
      V2 ~> E2 ~> V3
      builder
    }
}

