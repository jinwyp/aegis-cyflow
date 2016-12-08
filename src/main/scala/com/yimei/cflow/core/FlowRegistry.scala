package com.yimei.cflow.core


/**
  * Created by hary on 16/12/7.
  */
object FlowRegistry {

  private val registries = collection.mutable.Map[String, FlowGraph]()

  def register(flowType: String, graph: FlowGraph) = {
    registries(flowType) = graph
  }

  def getFlowGraph(flowType: String) = registries(flowType)
}