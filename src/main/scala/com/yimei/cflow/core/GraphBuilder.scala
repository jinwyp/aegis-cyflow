package com.yimei.cflow.core

import com.yimei.cflow.core.Flow._
import com.yimei.cflow.graph.ying.YingConfig._

/**
  * Created by hary on 16/12/6.
  */

object GraphBuilder {

  implicit class Ops(v: String) {
    def ~>(e: Edge)(implicit builder: GraphBuilder) = new OpsVE(v, e)
  }

  class OpsVE(vv: String, e: Edge) {
    def ~>(v: String)(implicit builder: GraphBuilder) = builder.lines = builder.lines + (e -> Array(vv, v))
  }

  def jsonGraph(state: State)(routine: GraphBuilder => GraphBuilder): Graph = {
    val builder = new GraphBuilder(Map[Edge, Array[String]]())
    routine(builder)
    Graph(builder.lines, state, pointDescription)
  }

  class GraphBuilder(var lines: Map[Edge, Array[String]])

}
