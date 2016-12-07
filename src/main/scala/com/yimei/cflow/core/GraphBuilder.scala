package com.yimei.cflow.core

import com.yimei.cflow.core.Flow.{Decision, Edge, State}
import com.yimei.cflow.core.FlowGraph.{EdgeLine, Graph}
import com.yimei.cflow.config.GlobalConfig._

/**
  * Created by hary on 16/12/6.
  */

object GraphBuilder {

  implicit class Ops(v: Decision) {
    def ~>(e: Edge)(implicit builder: GraphBuilder) = new OpsVE(v, e)
  }

  class OpsVE(vv: Decision, e: Edge) {
    def ~>(v: Decision)(implicit builder: GraphBuilder) = builder.lines = EdgeLine(vv, e, v) :: builder.lines
  }

  def jsonGraph(state: State)(routine: GraphBuilder => GraphBuilder): Graph = {
    val builder = new GraphBuilder(List.empty[EdgeLine]);
    routine(builder)
    Graph(builder.lines, state, pointDescription)
  }

  class GraphBuilder(var lines: List[EdgeLine])

}
