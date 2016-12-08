package com.yimei.cflow.core

import com.yimei.cflow.core.Flow.{Decision, Graph, State}


/**
  *
  */
trait FlowGraph {
  def getFlowInitial: Decision

  def getFlowGraph(state: State): Graph

  def getFlowType: String
}




