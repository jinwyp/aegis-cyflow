package com.yimei.cflow.graph.cang

import com.yimei.cflow.core.Flow.{Decision, Graph, State}
import com.yimei.cflow.core.FlowGraph

/**
  * Created by hary on 16/12/13.
  */
object CangGraph extends FlowGraph {
  /**
    * initial decision point
    *
    * @return
    */
  override def getFlowInitial: Decision = ???

  /**
    *
    * @param state
    * @return
    */
  override def getFlowGraph(state: State): Graph = ???

  override def registerAutoTask(): Unit = ???

  /**
    * flow type
    *
    * @return
    */
  override def getFlowType: String = ???
}
