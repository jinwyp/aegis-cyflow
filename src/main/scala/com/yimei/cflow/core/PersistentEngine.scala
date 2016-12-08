package com.yimei.cflow.core

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.{DataPoint, Graph, State, EdgeStart}

object PersistentEngine extends CoreConfig {
  def props[T <: FlowGraph](
                           graph: T,
                           flowId: String,
                           persistenceId: String,
                           modules: Map[String, ActorRef],
                           userId: String,
                           parties: Map[String, String] = Map()) = {

    Props(
      new PersistentEngine[T](
        graph,
        flowId,
        persistenceId,
        modules,
        userId,
        parties,
        config.getInt(s"flow.${graph.getFlowType}.timeout")))
  }
}

/**
  * @param graph
  * @param flowId
  * @param userId
  * @param parties
  * @param timeout
  * @tparam T
  */
class PersistentEngine[T <: FlowGraph](
    graph: T,
    flowId: String,
    val persistenceId: String,
    dependOn: Map[String, ActorRef],
    userId: String,
    parties: Map[String, String],
    timeout: Int) extends PersistentFlow(timeout) with ActorLogging {

  override var state = State(flowId, userId, parties, Map[String, DataPoint](), graph.getFlowInitial, Some(EdgeStart), Nil)
  override def queryStatus(state: State): Graph = graph.getFlowGraph(state)
  override def modules: Map[String, ActorRef] = dependOn
}

