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
      data_A ->(dataPointMap(data_A), (modules: Map[String, ActorRef]) => Props(new A(modules))),
      data_B ->(dataPointMap(data_B), (modules: Map[String, ActorRef]) => Props(new B(modules))),
      data_C ->(dataPointMap(data_C), (modules: Map[String, ActorRef]) => Props(new C(modules))),
      data_DEF ->(dataPointMap(data_DEF), (modules: Map[String, ActorRef]) => Props(new DEF(modules)))
    )

  // will be like this
  val k = FlowGraph.autoBuilder
    .actor("k").points("a", "b", "c").prop(modules => Props(new A(modules)))
    .actor("m").points("1", "2", "3").prop(modules => Props(new B(modules)))
    .actor("t").points("4", "5", "6").prop(modules => Props(new C(modules)))
    .done

  // decider builder


  override def getDeciders: Map[String, (State) => Arrow] =
    FlowGraph.deciderBuilder
    .decision("V0").is(V0)
    .decision("V1").is(V1)
    .decision("V2").is(V2)
    .decision("V3").is(V3)
    .decision("V4").is(V4)
    .decision("V5").is(V5)
    .done

  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = taskPointMap

  def getFlowInitial: Judge = Judge("V0")

  def getFlowGraph(state: State): Graph =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      import GraphBuilder._
      Judge("V0") ~> E1 ~> Judge("V1")
      Judge("V1") ~> E2 ~> Judge("V2")
      Judge("V2") ~> E3 ~> Judge("V3")
      Judge("V3") ~> E4 ~> Judge("V4")
      Judge("V4") ~> E5 ~> Judge("V5")
      Judge("V5") ~> EdgeStart ~> Judge("V3")
      builder
    }

  override def getFlowType: String = flow_ying

  val E1 = Edge(autoTasks = Array(data_A, data_B, data_C))
  val E2 = Edge(userTasks = Array(task_K_PU1,task_K_PG1))
  val E3 = Edge(partUTasks = Map(point_K_PU1->Array(task_PU)), partGTasks = Map(point_K_PG1->Array(task_PG)))
  val E4 = Edge(userTasks = Array(task_A))
  val E5 = Edge(autoTasks = Array(data_DEF))

  def V0(state: State): Arrow = {
    println("V0 -----E1----->V1")
    Arrow(Judge("V1"), Some(E1))
  }

  def V1(state: State): Arrow = Arrow(Judge("V2"), Some(E2))

  def V2(state: State): Arrow = {
    //当选择的user为fund-wangqiId，且group为fund-wqGroup是才通过
    if(state.points(point_K_PU1).value == "fund-wangqiId" && state.points(point_K_PG1).value == "fund-wqGroup" )
      Arrow(Judge("V3"),Some(E3))
    else
      Arrow(FlowFail, None)
  }

  def V3(state: State): Arrow = {

    //收集的pu_1,pu_2,pg-1,pg-2的总评分为100时通过
    state.points.filter(entry => List(point_PU1,point_PU2,point_PG1,point_PG2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 200 => Arrow(Judge("V4"), Some(E4))
      case _ => Arrow(FlowFail, None)
    }
  }

  def V4(state: State): Arrow = {
    // println(s"V4 state = $state")
    state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 100 => Arrow(Judge("V5"), Some(E5))
      case m =>
        println(s"V4 result = $m")
        Arrow(FlowFail, Some(EdgeStart))
    }
  }

  var count = 3

  def V5(state: State): Arrow = {

    state.points.filter(entry =>dataPointMap(data_DEF).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 150 => if(count>0) {
        count = count - 1
        Arrow(Judge("V3"), Some(EdgeStart))
      }
      else
        Arrow(FlowSuccess, None)
      case _ => Arrow(FlowFail, None)
    }

  }

  /*
val V0 = Judge("a")
val V1 = Judge("b")
val V2 = Judge("c")
val V3 = Judge("d")
val V4 = Judge("e")

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
 */


  /////////////////

//  case object V0 extends Judge {
//
//    override def decide(state: State): Arrow = {
//      println("V0 -----E1----->V1")
//      Arrow(V1, Some(E1))
//    }
//
//    override def toString = "V0"
//  }
//
//  case object V1 extends Judge {
//
//    override def decide(state: State): Arrow = Arrow(V2, Some(E2))
//
//    override def toString = "V1"
//  }
//
//  case object V2 extends Judge {
//
//    override def decide(state: State): Arrow = {
//      //当选择的user为fund-wangqiId，且group为fund-wqGroup是才通过
//      if(state.points(point_K_PU1).value == "fund-wangqiId" && state.points(point_K_PG1).value == "fund-wqGroup" )
//        Arrow(V3,Some(E3))
//      else
//        Arrow(FlowFail, None)
//    }
//
//    override def toString = "V2"
//  }

//  case object V3 extends Judge {
//
//    override def decide(state: State): Arrow = {
//
//      //收集的pu_1,pu_2,pg-1,pg-2的总评分为100时通过
//      state.points.filter(entry => List(point_PU1,point_PU2,point_PG1,point_PG2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
//        acc + entry._2.value.toInt
//      } match {
//        case 200 => Arrow(V4, Some(E4))
//        case _ => Arrow(FlowFail, None)
//      }
//
//    }
//
//    override def toString = "V3"
//  }

//  case object V4 extends Judge {
//
//    override def decide(state: State) = {
//      // println(s"V4 state = $state")
//      state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
//        acc + entry._2.value.toInt
//      } match {
//        case 100 => Arrow(V5, Some(E5))
//        case m =>
//          println(s"V4 result = $m")
//          Arrow(FlowFail, Some(EdgeStart))
//      }
//
//    }
//
//    override def toString = "V4"
//  }

//  case object V5 extends Judge {
//
//    var count = 3
//
//    override def decide(state: State) = {
//
//      state.points.filter(entry =>dataPointMap(data_DEF).contains(entry._1)).foldLeft(0) { (acc, entry) =>
//        acc + entry._2.value.toInt
//      } match {
//        case 150 => if(count>0) {
//          count = count - 1
//          Arrow(V3, Some(EdgeStart))
//        }
//        else
//          Arrow(FlowSuccess, None)
//        case _ => Arrow(FlowFail, None)
//      }
//
//    }
//
//    override def toString = "V5"
//  }


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
  //         \-------------------->  point_K_PU1,point_K_PG1        E2
  //         v2
  //           \------------------> [pu_1,pu_2](pg-1,pg-2)          E3
  //            V3
  //           /  \---------------> [UA1, UA2](task_A)              E4
  //          /    V4
  //         /      \-------------> [UB1, UB2](partTask_A)          E5
  //         --<----V5
  //             |
  //             |---------------->                                 EdgeStart
  ///////////////////////////////////////////////////////////////////////////////////////

}


