package com.yimei.cflow.graph.cang

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow.{Arrow, Graph, State}
import com.yimei.cflow.core.FlowGraph
import com.yimei.cflow.core.FlowRegistry.AutoProperty

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


  /**
    *
    */
  override def getAutoTask: Map[String, AutoProperty] = ???

  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = ???

  /**
    * 所有决策点
    */
  override def getDeciders: Map[String, (State) => Arrow] = ???
}
