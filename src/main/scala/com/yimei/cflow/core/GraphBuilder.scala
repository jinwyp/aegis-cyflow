package com.yimei.cflow.core

import com.yimei.cflow.core.Flow.{Decision, Edge, State}
import com.yimei.cflow.core.Flow.Graph
import com.yimei.cflow.config.GlobalConfig._

/**
  * Created by hary on 16/12/6.
  */

object GraphBuilder {

  implicit class Ops(v: Decision) {
    def ~>(e: Edge)(implicit builder: GraphBuilder) = new OpsVE(v, e)
  }

  class OpsVE(vv: Decision, e: Edge) {
    def ~>(v: Decision)(implicit builder: GraphBuilder) = builder.lines = builder.lines + (e -> Array(vv, v))
  }

  def jsonGraph(state: State)(routine: GraphBuilder => GraphBuilder): Graph = {
    val builder = new GraphBuilder(Map[Edge, Array[Decision]]());
    routine(builder)
    Graph(builder.lines, state, pointDescription)
  }

  class GraphBuilder(var lines: Map[Edge, Array[Decision]])

}
