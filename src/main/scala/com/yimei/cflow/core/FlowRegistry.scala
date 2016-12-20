package com.yimei.cflow.core

import java.lang.reflect.Method

import com.yimei.cflow.core.Flow._


/**
  * Created by hary on 16/12/7.
  */
object FlowRegistry {

  private val registries = collection.mutable.Map[String, FlowGraph]()

  // flowType -> actorName -> AutoProperty
  var autoTask: Map[String, Map[String, Array[String]]]  = Map()

  // flowType -> userTask -> points
  var userTask: Map[String, Map[String, Array[String]]] = Map()

  // flowType -> deciderName -> behavior
  var deciders: Map[String, Map[String, State => Arrow]] = Map()

  // flowType -> edgeName -> edge
  var edges: Map[String,Map[String,Edge]] = Map()

  // flowType -> vertex -> in Edges
  var inEdges: Map[String, Map[String, Array[String]]] = Map()

  // flowType -> Graph
  var graphs: Map[String, Graph] = Map()

  // flowType -> actorName -> Method
  var autoMeth: Map[String, Map[String, Method]]  = Map()

  // flowType -> AnyRef
  var jarMap: Map[String, AnyRef] = Map()

  def register(flowType: String, graph: FlowGraph) = {

    registries(flowType) = graph

    autoTask = autoTask + (flowType -> graph.autoTasks)

    userTask = userTask + (flowType -> graph.userTasks)

    autoMeth = autoMeth + (flowType -> graph.autoMethods)

    deciders = deciders + (flowType -> graph.deciders)

    jarMap   = jarMap   + (flowType -> graph.moduleJar)

    edges = Map(flowType -> graph.edges)
  }

  def getFlowGraph(flowType: String) = registries(flowType)

}
