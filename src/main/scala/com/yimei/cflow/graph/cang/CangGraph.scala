package com.yimei.cflow.graph.cang

import java.lang.reflect.Method

import com.yimei.cflow.core.Flow.{Edge, Graph, State}
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


  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = ???


  /**
    *
    * @return
    */
  override def getAutoTask: Map[String, Array[String]] = ???

  /**
    *
    */
  override def getEdges: Map[String, Edge] = ???

  /**
    *
    * @return
    */
  override def getAutoMeth: Map[String, Method] = ???

  /**
    *
    * @return
    */
  override def getDeciMeth: Map[String, Method] = ???

  /**
    *
    * @return
    */
  override def getGraphJar: AnyRef = ???
}
