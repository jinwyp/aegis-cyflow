package com.yimei.cflow.core

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.{DataPoint, State}

/**
  * Created by hary on 16/12/6.
  */

object Engine extends CoreConfig {



  def props[T <: FlowGraph](graph: T, flowId: String, modules: Map[String, ActorRef],
                            userId: String,
                            parties: Map[String, String] = Map()) = {
    Props(new Engine[T](graph, flowId, modules, userId, parties, config.getInt(s"flow.${graph.getFlowType}.timeout")))
  }
}

/**
  * @param graph    flow graph
  * @param userId   用户id
  * @param parties  参与方消息
  * @param timeout
  * @tparam T
  */
class Engine[T <: FlowGraph]( graph: T,
                              flowId: String,
                              dependOn: Map[String, ActorRef],
                              userId: String,
                              parties: Map[String, String],
                              timeout: Int) extends Flow with ActorLogging {

  override var state = State(flowId, userId, parties, Map[String, DataPoint](), graph.getFlowInitial, Nil)
  override def queryStatus(state: State): String = graph.getFlowJson(state)
  override def modules: Map[String, ActorRef] = dependOn
}

