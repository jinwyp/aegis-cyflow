package com.yimei.cflow.graph.ying

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.{FlowGraph}
import com.yimei.cflow.graph.ying.YingConfig._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by hary on 16/12/1.
  */
object YingGraph extends FlowGraph {

  override val flowType: String = flow_ying
  override val timeout: Long = 15
  override val userTasks: Map[String, TaskInfo] = taskPointMap
  override val autoTasks: Map[String, TaskInfo] = autoPointMap
  override val flowInitial: String = J0

  override val points: Map[String, String] = pointDescription
  override val vertices: Map[String, String] = Map(
    "V0" -> "V0",
    "V1" -> "V1",
    "V2" -> "V2",
    "V3" -> "V3",
    "V4" -> "V4",
    "V5" -> "V5"
  )

  val E1 = Edge(name = "E1", begin = "V0", end = "V1", autoTasks = List(auto_A, auto_B, auto))
  val E2 = Edge(name = "E2", begin = "V1", end = "V2", userTasks = List(task_K_PU1, task_K_PG1))
  val E3 = Edge(name = "E3", begin = "V2", end = "V3", partUTasks = List(PartUTask(point_KPU_1, List(task_PU))), partGTasks = List(PartGTask(point_KPG_1, List(task_PG))))
  val E4 = Edge(name = "E4", begin = "V3", end = "V4", userTasks = List(task_A))
  val E5 = Edge(name = "E5", begin = "V4", end = "V5", autoTasks = List(auto_DEF))
  val E6 = Edge(name = "E6", begin = "V5", end = "V3")
  val start = Edge(name = "start", end = "V0")   // 起始边

  override val blueprint: Graph = graph(null)

  override val edges: Map[String, Edge] = Map(
    "start" -> start,
    //"success" -> EdgeSuccess,
    //"fail" -> EdgeFail,
    "E1" -> E1,
    "E2" -> E2,
    "E3" -> E3,
    "E4" -> E4,
    "E5" -> E5,
    "E6" -> E6
  )
  override val inEdges: Map[String, Array[String]] = Map(
    J0 -> Array("start"),
    J1 -> Array("E1"),
    J2 -> Array("E1"),
    J3 -> Array("E1", "E6"),
    J4 -> Array("E1"),
    J5 -> Array("E1")
  )


  override val pointEdges: Map[String, String] = pointEdgesImpl

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

  def V0(state: State): Seq[Arrow] = Seq(Arrow(J1, Some("E1")))
  def V1(state: State): Seq[Arrow] = Seq(Arrow(J2, Some("E2")))
  def V2(state: State): Seq[Arrow] = Seq(Arrow(J3, Some("E3")))
  def V3(state: State): Seq[Arrow] = {
    //收集的pu_1,pu_2,pg-1,pg-2的总评分为100时通过
    state.points.filter(entry => List(point_PU1, point_PU2, point_PG1, point_PG2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 200 => Seq(Arrow(J4, Some("E4")))
      case _ => Seq(ArrowFail)
    }
  }

  def V4(state: State): Seq[Arrow] = {
    state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 100 => Seq(Arrow(J5, Some("E5")))
      case m =>
        // Arrow(FlowFail, Some(EdgeStart))
        Seq(ArrowFail)
    }
  }

  var count = 0

  def V5(state: State): Seq[Arrow] = {
    state.points.filter(entry => autoPointMap(auto_DEF).points.contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 150 => if (count > 0) {
        count = count - 1
        Seq(Arrow(J3, Some("E6")))
      }
      else
        Seq(ArrowSuccess)
      case _ => Seq(ArrowFail)
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


