package com.yimei.cflow.graph.cang

import com.yimei.cflow.core.Flow.{Edge, Graph, State}
import com.yimei.cflow.core.FlowGraph
import com.yimei.cflow.graph.cang.CangConfig._

/**
  * Created by hary on 16/12/13.
  */
object CangGraph extends FlowGraph {

  override def getTimeout: Long = 1 * 60 * 60

  override def getFlowInitial: String = judge_afterStart

  override def getFlowType: String = flow_cang

  override def getUserTask: Map[String, Array[String]] = taskPointMap

  override def getAutoTask: Map[String, Array[String]] = dataPointMap


  /////////////////////////////////////////////////////////////
  //  定义点
  /////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////
  //  定义边
  /////////////////////////////////////////////////////////////

  override def getFlowGraph(state: State): Graph = ???

  override def getEdges: Map[String, Edge] = ???
}
