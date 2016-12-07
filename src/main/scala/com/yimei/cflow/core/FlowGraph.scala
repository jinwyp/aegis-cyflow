package com.yimei.cflow.core

import com.yimei.cflow.core.Flow.{Decision, Edge, State}
import com.yimei.cflow.core.FlowGraph.Graph

/**
  * Created by hary on 16/12/1.
  */
object FlowGraph {


  case class EdgeLine(begin: Decision, description: Edge, end: Decision)

  case class Graph(edges: List[EdgeLine], state: State, dataDescription: Map[String, String])

  import FlowProtocol._

  implicit val edgeLineFormat = jsonFormat3(EdgeLine)
  implicit val graphFormat = jsonFormat3(Graph)
}

/**
  *
  */
trait FlowGraph {
  def getFlowInitial: Decision

  def getFlowGraph(state: State): Graph

  def getFlowType: String
}




