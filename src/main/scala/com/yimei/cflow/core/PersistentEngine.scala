package com.yimei.cflow.core

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.{DataPoint, State}

object PersistentEngine extends CoreConfig {
  def props[T <: FlowGraph](graph: T, flowId: String, modules: Map[String, ActorRef],
                           userId: String,
                           parties: Map[String, String] = Map()) = {
    Props(
      new PersistentEngine[T](
        graph,
        flowId,
        modules,
        userId,
        parties,
        config.getInt(s"flow.${graph.getFlowName}.timeout")))
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
    dependOn: Map[String, ActorRef],
    userId: String,
    parties: Map[String, String],
    timeout: Int) extends PersistentFlow(timeout) with ActorLogging {
  override val persistenceId = flowId
  override var state = State(flowId, userId, parties, Map[String, DataPoint](), graph.getFlowInitial, Nil)
  override def queryStatus(state: State): String = graph.getFlowJson(state)
  override def modules: Map[String, ActorRef] = dependOn
}

