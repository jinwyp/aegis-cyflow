package com.yimei.cflow.core

import java.lang.reflect.Method

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow._


/**
  * Created by hary on 16/12/7.
  */
object FlowRegistry {

  // AutoActor Property
  case class AutoProperty(points: Array[String], prop: Map[String, ActorRef] => Props)

  private val registries = collection.mutable.Map[String, FlowGraph]()

  // flowType -> actorName -> AutoProperty
  var autoTask: Map[String, Map[String, AutoProperty]]  = Map()

  // flowType -> userTask -> points
  var userTask: Map[String, Map[String, Array[String]]] = Map()

  // flowType -> deciderName -> behavior
  var deciders: Map[String, Map[String, State => Arrow]] = Map()

  // flowType -> edgeName -> edge
  var edges: Map[String,Map[String,Edge]] = Map()

  // flowType -> Graph  and Graph should be:
  // case class Graph(edges: Map[String, EdgeDescription], points: Map[String, String])
  // PersistentFlow does not need Graph parameters
  // todo
  var graphs: Map[String, Graph] = Map()

  // flowType -> actorName -> Method
  var autoMeth: Map[String, Map[String, Method]]  = Map()
  var deciMeth: Map[String, Map[String, Method]]  = Map()

  // flowType -> AnyRef
  var jarMap: Map[String, AnyRef] = Map()

  def register(flowType: String, graph: FlowGraph) = {
    registries(flowType) = graph
    autoTask = autoTask + (flowType -> graph.getAutoTask)
    userTask = userTask + (flowType -> graph.getUserTask)
    deciders = deciders + (flowType -> graph.getDeciders)

    // todo hary
    autoMeth = autoMeth + ( flowType -> graph.getAutoMap)
    deciMeth = deciMeth + ( flowType -> graph.getDeciMap)
    jarMap = jarMap + (flowType -> graph.getGraphJar)
    // todo end

    edges = Map(flowType-> graph.getEdges)
  }

  def getFlowGraph(flowType: String) = registries(flowType)

}
