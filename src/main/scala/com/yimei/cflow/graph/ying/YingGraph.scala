package com.yimei.cflow.graph.ying

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.{FlowGraph, GraphBuilder}
import com.yimei.cflow.graph.ying.YingConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by hary on 16/12/1.
  */
object YingGraph extends FlowGraph {


  override def getTimeout: Long = 15

  /**
    * 注册用户任务
    */
  override def getUserTask: Map[String, Array[String]] = taskPointMap


  /**
    *
    * @return
    */
  override def getAutoTask: Map[String, Array[String]] = autoPointMap

  /**
    *
    * @return
    */
  def getFlowInitial: String = J0

  /**
    *
    * @param state
    * @return
    */
  def getFlowGraph(state: State): Graph =
    GraphBuilder.jsonGraph(state, judgeDecription, pointDescription, autoPointMap, taskPointMap) { implicit builder =>
      import GraphBuilder._
      J0 ~> E1 ~> J1
      J1 ~> E2 ~> J2
      J2 ~> E3 ~> J3
      J3 ~> E4 ~> J4
      J4 ~> E5 ~> J5
      J5 ~> E6 ~> J3
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
  val E2 = Edge("E2", userTasks = List(task_K_PU1, task_K_PG1))
  val E3 = Edge("E3", partUTasks = List(PartUTask(point_KPU_1, List(task_PU))), partGTasks = List(PartGTask(point_KPG_1, List(task_PG))))
  val E4 = Edge("E4", userTasks = List(task_A))
  val E5 = Edge("E5", autoTasks = List(auto_DEF))
  val E6 = Edge("E6")

  def A(cmd: CommandAutoTask): Future[Map[String, String]] = Future {
    Map("A" -> "50")
  }

  def B(cmd: CommandAutoTask): Future[Map[String, String]] = Future {
    Map("B" -> "50")
  }

  def C(cmd: CommandAutoTask): Future[Map[String, String]] = Future {
    Map("C" -> "50")
  }

  def DEF(cmd: CommandAutoTask): Future[Map[String, String]] = Future {
    Map("D" -> "50", "E" -> "50", "F" -> "50")
  }

  def V0(state: State): Arrow = Arrow(J1, Some("E1"))

  def V1(state: State): Arrow = Arrow(J2, Some("E2"))

  def V2(state: State): Arrow = Arrow(J3, Some("E3"))

  def V3(state: State): Arrow = {
    //收集的pu_1,pu_2,pg-1,pg-2的总评分为100时通过
    state.points.filter(entry => List(point_PU1, point_PU2, point_PG1, point_PG2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 200 => Arrow(J4, Some("E4"))
      case _ => Arrow(FlowFail, None)
    }
  }

  def V4(state: State): Arrow = {
    state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 100 => Arrow(J5, Some("E5"))
      case m =>
        // Arrow(FlowFail, Some(EdgeStart))
        Arrow(FlowFail, None)
    }
  }

  var count = 3

  def V5(state: State): Arrow = {
    state.points.filter(entry => autoPointMap(auto_DEF).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 150 => if (count > 0) {
        count = count - 1
        Arrow(J3, Some("E6"))
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


