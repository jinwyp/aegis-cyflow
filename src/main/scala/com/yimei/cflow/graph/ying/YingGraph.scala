package com.yimei.cflow.graph.ying

import akka.actor.Props
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.FlowRegistry.AutoProperty
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
  override def getAutoTask: Map[String, AutoProperty] = FlowGraph.autoBuilder
    .actor(auto_A)  .points(dataPointMap(auto_A))  .prop(modules => Props(new A(modules)))
    .actor(auto_B)  .points(dataPointMap(auto_B))  .prop(modules => Props(new B(modules)))
    .actor(auto)    .points(dataPointMap(auto))    .prop(modules => Props(new C(modules)))
    .actor(auto_DEF).points(dataPointMap(auto_DEF)).prop(modules => Props(new DEF(modules)))
    .done

  /**
    *
    * @return
    */
  override def getDeciders: Map[String, (State) => Arrow] = FlowGraph.deciderBuilder
    .decision("V0").is(J0)
    .decision("V1").is(J1)
    .decision("V2").is(J2)
    .decision("V3").is(J3)
    .decision("V4").is(J4)
    .decision("V5").is(J5)
    .done

  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = taskPointMap

  /**
    *
    * @return
    */
  def getFlowInitial: String = V0

  /**
    *
    * @param state
    * @return
    */
  def getFlowGraph(state: State): Graph =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      import GraphBuilder._
      V0 ~> E1 ~> V1
      V1 ~> E2 ~> V2
      V2 ~> E3 ~> V3
      V3 ~> E4 ~> V4
      V4 ~> E5 ~> V5
      V5 ~> E6 ~> V3
      builder
    }

  override def getFlowType: String = flow_ying


  /**
    *
    */
  override def getEdges: Map[String, Edge] = Map(
    "E1" -> E1,
    "E2" -> E2,
    "E3" -> E3,
    "E4" -> E4,
    "E5" -> E5,
    "E6" -> E6
  )

  val E1 = Edge("E1", autoTasks = List(auto_A, auto_B, auto))
  val E2 = Edge("E2", userTasks = List(task_K_PU1,task_K_PG1))
  val E3 = Edge("E3", partUTasks = List(PartUTask(point_KPU_1,List(task_PU))), partGTasks = List(PartGTask(point_KPG_1,List(task_PG))))
  val E4 = Edge("E4", userTasks = List(task_A))
  val E5 = Edge("E5", autoTasks = List(auto_DEF))
  val E6 = Edge("E6")

  def J0(state: State): Arrow = {
    Arrow(V1, Some("E1"))
  }

  def J1(state: State): Arrow = Arrow(V2, Some("E2"))

  def J2(state: State): Arrow = {

    Arrow(V3,Some("E3"))
//    //当选择的user为fund-wangqiId，且group为fund-wqGroup是才通过
//    if(state.points(point_K_PU1).value == "fund-wangqiId" && state.points(point_K_PG1).value == "fund-wqGroup" )
//      Arrow(V3,Some(E3))
//    else
//      Arrow(FlowFail, None)
  }

  def J3(state: State): Arrow = {

    //收集的pu_1,pu_2,pg-1,pg-2的总评分为100时通过
    state.points.filter(entry => List(point_PU1,point_PU2,point_PG1,point_PG2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 200 => Arrow(V4, Some("E4"))
      case _ => Arrow(FlowFail, None)
    }
  }

  def J4(state: State): Arrow = {
    state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 100 => Arrow(V5, Some("E5"))
      case m =>
       // Arrow(FlowFail, Some(EdgeStart))
        Arrow(FlowFail, None)
    }
  }

  var count = 3

  def J5(state: State): Arrow = {

    state.points.filter(entry =>dataPointMap(auto_DEF).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 150 => if(count>0) {
        count = count - 1
        //Arrow(V3, Some(EdgeStart))
        Arrow(V3,Some("E6"))
      }
      else
        Arrow(FlowSuccess, None)
      case _ => Arrow(FlowFail, None)
    }

  }

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


