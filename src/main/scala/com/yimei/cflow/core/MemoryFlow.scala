package com.yimei.cflow.core

import java.util.{Date, UUID}

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.FlowRegistry._

object MemoryFlow {
  /**
    *
    * @param graph   flow graph
    * @param flowId  flow Id  (flowType-userType*userId-persistenceId)
    * @param modules injected dependent modules
    * @param guid    global userId (userType + userId)
    * @return
    */
  def props(graph: FlowGraph, flowId: String, modules: Map[String, ActorRef], guid: String, initData: Map[String, String]) = {
    Props(new MemoryFlow(graph, flowId, modules, guid, initData))
  }

}

/**
  *
  * @param graph  flow graph
  * @param flowId flow id(flowType-userType-userId-persistenceId)
  * @param guid   global userId (userType + userId)
  */
class MemoryFlow(
                  val graph: FlowGraph,
                  flowId: String,
                  modules: Map[String, ActorRef],
                  guid: String,
                  initData: Map[String, String]) extends AbstractFlow {

  import com.yimei.cflow.api.models.flow._


  val initPoints = initData.map{ entry =>
    (entry._1, DataPoint(entry._2, None, None, "init", new Date().getTime, false))
  }

  override var state: State = State(flowId, guid, initPoints, Map("start" -> true), Nil, graph.flowType)

  override def genGraph(state: State): Graph = graph.graph(state)

  //   override def modules: Map[String, ActorRef] = dependOn

  override def receive: Receive = commonBehavior orElse serving

  // servicable
  val serving: Receive = ???

}
