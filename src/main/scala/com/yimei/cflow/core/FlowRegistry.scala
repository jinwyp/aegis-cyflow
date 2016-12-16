package com.yimei.cflow.core

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow.{Arrow, State}


/**
  * Created by hary on 16/12/7.
  */
object FlowRegistry {

  // AutoActor Property
  case class AutoProperty(points: Array[String], prop: Map[String, ActorRef] => Props)

  private val registries = collection.mutable.Map[String, FlowGraph]()

  var autoTask: Map[String, Map[String, AutoProperty]]  = Map()

  // flowType -> userTask -> points
  var userTask: Map[String, Map[String, Array[String]]] = Map()

  // flowType -> deciderName -> behavior
  var deciders: Map[String, Map[String, State => Arrow]] = Map()

  def register(flowType: String, graph: FlowGraph) = {
    registries(flowType) = graph
    autoTask = autoTask + (flowType -> graph.getAutoTask)
    userTask = userTask + (flowType -> graph.getUserTask)
    deciders = deciders + (flowType -> graph.getDeciders)
  }

  def getFlowGraph(flowType: String) = registries(flowType)

}
