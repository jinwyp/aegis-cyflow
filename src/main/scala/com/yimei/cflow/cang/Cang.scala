package com.yimei.cflow.cang

import akka.actor.{ActorLogging, Props}
import com.yimei.cflow.core.{Flow, GraphBuilder}
import com.yimei.cflow.core.Flow.{DataPoint, State}
import com.yimei.cflow.cang.CangGraph._
import GraphBuilder._

object Cang {
  def props(flowId: String) = Props(new Cang(flowId))
}

class Cang(flowId: String) extends Flow with ActorLogging {
  override var state = State(flowId, Map[String, DataPoint](), V0, Nil)

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

