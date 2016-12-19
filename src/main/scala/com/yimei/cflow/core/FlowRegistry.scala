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

  // flowType -> Graph
  var graphs: Map[String, Graph] = Map()

  // flowType -> actorName -> Method
  var autoMeth: Map[String, Map[String, Method]]  = Map()

  // flowType -> AnyRef
  var jarMap: Map[String, AnyRef] = Map()

  def register(flowType: String, graph: FlowGraph) = {
    registries(flowType) = graph

    autoTask = autoTask + (flowType -> graph.getAutoTask)

    userTask = userTask + (flowType -> graph.getUserTask)

    autoMeth = autoMeth + (flowType -> graph.getAutoMeth)

    deciders = deciders + (flowType -> graph.getDeciMeth.map { entry =>
      val behavior: State => Arrow  = (state: State)  =>
        entry._2.invoke(graph.getGraphJar, state).asInstanceOf[Arrow]
      (entry._1, behavior)
    })


    jarMap   = jarMap   + (flowType -> graph.getGraphJar)

    edges = Map(flowType-> graph.getEdges)
  }

  def getFlowGraph(flowType: String) = registries(flowType)

}
