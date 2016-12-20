package com.yimei.cflow.core

import java.lang.reflect.Method

import com.yimei.cflow.core.Flow._


/**
  * Created by hary on 16/12/7.
  */
object FlowRegistry {

  var registries = Map[String, FlowGraph]()

  def register(flowType: String, graph: FlowGraph) = {
    registries += (flowType -> graph)
  }

  def flowGraph(flowType: String) = registries(flowType)

}
