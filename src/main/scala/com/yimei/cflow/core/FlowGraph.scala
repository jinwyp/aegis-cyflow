package com.yimei.cflow.core

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow.{Decision, Graph, State}


/**
  *
  */
trait FlowGraph {
  /**
    * initial decision point
    * @return
    */
  def getFlowInitial: Decision

  /**
    *
    * @param state
    * @return
    */
  def getFlowGraph(state: State): Graph

  /**
    * flow type
    * @return
    */
  def getFlowType: String

  /**
    *
    */
  def getAutoTask: Map[String, (Array[String], Map[String, ActorRef] => Props)]

  /**
    * 注册用户任务
    */
  def getUserTask: Map[String, Array[String]]

}




