package com.yimei.cflow.engine

import com.yimei.cflow.engine.graph.FlowGraph

/**
  * Created by hary on 16/12/7.
  */
object FlowRegistry {

  var registries = Map[String, FlowGraph]()

  def register(flowType: String, graph: FlowGraph) = {
    if( registries.contains(flowType)) {
      false
    } else {
      registries += (flowType -> graph)
      true
    }
  }

  def flowGraph(flowType: String) = registries(flowType)

}
