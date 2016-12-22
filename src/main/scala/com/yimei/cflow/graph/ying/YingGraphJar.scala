package com.yimei.cflow.graph.ying

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.api.models.flow._
import com.yimei.cflow.graph.ying.YingConfig._

import scala.concurrent.Future

object YingGraphJar {

  import scala.concurrent.ExecutionContext.Implicits.global

//  def V0(state: State): Arrow = Arrow(J1, Some("E1"))
//
//  def V1(state: State): Arrow = Arrow(J2, Some("E2"))
//
//  def V2(state: State): Arrow = Arrow(J3, Some("E3"))

  val ArrowFail = Arrow("fail", None)
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
      case m => Seq(ArrowFail)
    }
  }

  var count = 3

  def V5(state: State): Seq[Arrow] = {
    state.points.filter(entry => autoPointMap(auto_DEF).points.contains(entry._1)).foldLeft(0) { (acc, entry) =>
      acc + entry._2.value.toInt
    } match {
      case 150 => if (count > 0) {
        count = count - 1
        Seq(Arrow(J3, Some("E6")))
      }
      else
        Seq(Arrow("success", None))

      case m => Seq(ArrowFail)
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

