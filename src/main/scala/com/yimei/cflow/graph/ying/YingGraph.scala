package com.yimei.cflow.graph.ying

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.{FlowGraph, GraphBuilder}
import com.yimei.cflow.graph.ying.YingConfig._

/**
  * Created by hary on 16/12/1.
  */
object YingGraph extends FlowGraph {

  import AutoActors._
  /**
    *
    */
  override def getAutoTask: Map[String, (Array[String], Map[String, ActorRef] => Props)] =
    Map(
      data_A -> (dataPointMap(data_A), (modules: Map[String, ActorRef]) => Props(new A(modules))),
      data_B -> (dataPointMap(data_B), (modules: Map[String, ActorRef]) => Props(new B(modules))),
      data_C -> (dataPointMap(data_C), (modules: Map[String, ActorRef]) => Props(new C(modules))),
      data_DEF -> (dataPointMap(data_DEF), (modules: Map[String, ActorRef]) => Props(new DEF(modules))),
      data_GHK -> (dataPointMap(data_GHK), (modules: Map[String, ActorRef]) => Props(new GHK(modules)))
    )

  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = taskPointMap

  def getFlowInitial: Decision = V0

  def getFlowGraph(state: State) =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      import GraphBuilder._
      V0 ~> E1 ~> V1
      V1 ~> E2 ~> V2
      V2 ~> E3 ~> V3
      V3 ~> E4 ~> V4
      V4 ~> E5 ~> V5
      V5 ~> EdgeStart ~> V3
      builder
    }


  override def getFlowType: String = flow_ying


  val E1 = Edge(autoTasks = Array(data_A, data_B, data_C))
  val E2 = Edge(autoTasks = Array(data_DEF))
  val E3 = Edge(autoTasks = Array(data_GHK))
  val E4 = Edge(userTasks = Array(task_A))
  val E5 = Edge(partUTasks = partUserTaskMap)


  /////////////////
  case object V0 extends Judge {

    override def decide(state: State): Arrow = {
      println("V0 -----E1----->V1")
      Arrow(V1, Some(E1))
    }

    override def toString = "V0"
  }

  case object V1 extends Judge {

    override def decide(state: State): Arrow = Arrow(V2, Some(E2))

    override def toString = "V1"
  }

  case object V2 extends Judge {

    override def decide(state: State): Arrow = {
      state.points.filter(entry => dataPointMap(data_DEF).contains(entry._1)).foldLeft(0) { (acc, entry) =>
        acc + entry._2.value.toInt
      } match {
        case 150 => Arrow(V3, Some(E3))
        case _ => Arrow(FlowFail, None)
      }
    }

    override def toString = "V2"
  }

  case object V3 extends Judge {

    override def decide(state: State): Arrow = {

      state.points.filter(entry => dataPointMap(data_GHK).contains(entry._1)).foldLeft(0) { (acc, entry) =>
        acc + entry._2.value.toInt
      } match {
        case 150 => Arrow(V4, Some(E4))
        case _ => Arrow(FlowFail, None)
      }

    }

    override def toString = "V3"
  }

  case object V4 extends Judge {

    override def decide(state: State) = {
      // println(s"V4 state = $state")
      state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
        acc + entry._2.value.toInt
      } match {
        case 100 => Arrow(V5, Some(E5))
        case m =>
          println(s"V4 result = $m")
          Arrow(FlowFail, Some(EdgeStart))
      }

    }

    override def toString = "V4"
  }

  case object V5 extends Judge {

    override def decide(state: State) = {

      state.points.filter(entry => List(point_U_B1, point_U_B2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
        acc + entry._2.value.toInt
      } match {
        case 100 => Arrow(V3, Some(EdgeStart))
        case _ => Arrow(FlowFail, None)
      }

    }

    override def toString = "V5"
  }


  ///////////////////////////////////////////////////////////////////////////////////////
  //      \ ----------------------> always true                     VoidEdge
  //      V0
  //       \ ---------------------> A(data_A) B(data_B) C(data_C)   E1
  //       V1
  //         \--------------------> [D, E, F](data_DEF)             E2
  //         v2
  //           \------------------> [G, H, K]                       E3
  //            V3
  //           /  \---------------> [UA1, UA2](task_A)              E4
  //          /    V4
  //         /      \-------------> [UB1, UB2](task_B)              E5
  //         --<----V5
  //             |
  //             |---------------->                                 EdgeStart
  ///////////////////////////////////////////////////////////////////////////////////////


  ///////////////////////////////////////////////////////////////////////////////////////
  //      \ ----------------------> always true                     VoidEdge
  //      V0
  //       \ ---------------------> A(data_A) B(data_B) C(data_C)   E1
  //       V1
  //         \--------------------> [D, E, F](data_DEF)             E2
  //         v2
  //           \------------------> [G, H, K]                       E3
  //            V3
  //           /  \---------------> [UA1, UA2](task_A)              E4
  //          /    V4
  //         /      \-------------> [UB1, UB2](task_B)              E5
  //         --<----V5
  //             |
  //             |---------------->                                 EdgeStart
  ///////////////////////////////////////////////////////////////////////////////////////

}


