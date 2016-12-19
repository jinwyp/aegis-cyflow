package com.yimei.cflow.graph.ying

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.graph.ying.YingConfig._

import scala.concurrent.Future

object YingGraphJar {

  import scala.concurrent.ExecutionContext.Implicits.global

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
}

