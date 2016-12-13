package com.yimei.cflow.core

import com.yimei.cflow.core.Flow.{Decision, Graph, State}


/**
  *
  */
trait FlowGraph {
  /**
    * initial decision point
    * @return
    */
  def getFlowInitial: Decision

  /**
    *
    * @param state
    * @return
    */
  def getFlowGraph(state: State): Graph

  /**
    * flow type
    * @return
    */
  def getFlowType: String

  def registerAutoTask(): Unit
}




