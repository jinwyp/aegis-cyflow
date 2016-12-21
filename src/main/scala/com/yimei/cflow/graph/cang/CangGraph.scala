package com.yimei.cflow.graph.cang

import com.yimei.cflow.core.Flow.{Edge, Graph, State, TaskInfo}
import com.yimei.cflow.core.FlowGraph
import com.yimei.cflow.graph.cang.CangConfig._

/**
  * Created by hary on 16/12/13.
  */
object CangGraph extends FlowGraph {

  override val points: Map[String, String] = Map()

  override val vertices: Map[String, String] = Map()

  override val timeout: Long = 1 * 60 * 60

  override val flowInitial: String = judge_afterStart

  override val flowType: String = flow_cang

  override val userTasks: Map[String, TaskInfo] = taskPointMap

  override val autoTasks: Map[String, TaskInfo] = dataPointMap


  /////////////////////////////////////////////////////////////
  //  定义点
  /////////////////////////////////////////////////////////////

  /////////////////////////////////////////////////////////////
  //  定义边
  /////////////////////////////////////////////////////////////

  override def graph(state: State): Graph = ???

  override val edges: Map[String, Edge] = ???
  override val pointEdges: Map[String, String] = Map()
}
