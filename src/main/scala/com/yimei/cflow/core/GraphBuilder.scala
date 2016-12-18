package com.yimei.cflow.core

import com.yimei.cflow.core.Flow._
import com.yimei.cflow.graph.ying2.YingConfig._

/**
  * Created by hary on 16/12/6.
  */

object GraphBuilder {

  implicit class Ops(v: String) {
    def ~>(e: Edge)(implicit builder: GraphBuilder) = new OpsVE(v, e)
  }

  class OpsVE(vv: String, e: Edge) {
    def ~>(v: String)(implicit builder: GraphBuilder) =
      builder.lines = builder.lines + (e.name -> EdgeDescription(
        autoTasks = e.autoTasks,
        userTasks = e.userTasks,
        partUTasks = e.partUTasks,
        partGTasks = e.partGTasks,
        vv,
        v))
  }

  def jsonGraph(state: State)(routine: GraphBuilder => GraphBuilder): Graph = {
    val builder = new GraphBuilder(Map[String, EdgeDescription]())
    routine(builder)
    Graph(builder.lines, state, pointDescription)
  }

  class GraphBuilder(var lines: Map[String, EdgeDescription])

}
