package com.yimei.cflow.graph.cang

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow.{Graph, State}
import com.yimei.cflow.core.FlowGraph

/**
  * Created by hary on 16/12/13.
  */
object CangGraph extends FlowGraph {
  /**
    * initial decision point
    *
    * @return
    */
  override def getFlowInitial: String = ???

  /**
    *
    * @param state
    * @return
    */
  override def getFlowGraph(state: State): Graph = ???


  /**
    * flow type
    *
    * @return
    */
  override def getFlowType: String = ???

  /**
    *
    */
  override def getAutoTask: Map[String, (Array[String], (Map[String, ActorRef]) => Props)] = ???

  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = ???
}
