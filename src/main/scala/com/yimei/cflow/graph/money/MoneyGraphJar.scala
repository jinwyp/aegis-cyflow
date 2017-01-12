package com.yimei.cflow.graph.money

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.models.flow.{Arrow, _}
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.api.models.auto.CommandAutoTask

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/23.
  */
object MoneyGraphJar {

  import scala.concurrent.ExecutionContext.Implicits.global

  val ArrowFail = Arrow("fail", None)
  val ArrowSuccess = Arrow("success", None)

  def V0(state: State) = Seq(Arrow("V1", Some("E1")))
  def V3(state: State) = Seq(Arrow("V4", Some("E4")))
  def V4(state: State) = Seq(Arrow("V5", Some("E5")))
  def V5(state: State) = Seq(Arrow("V6", Some("E6")))
  def V1(state: State) = {
    if (state.points("SuccessRate").value.toDouble < 0.5) {
      Seq(ArrowFail)
    } else {
      Seq(Arrow("V4", Some("E2")), Arrow("V3", Some("E3")))
    }
  }
  def V6(state: State) = {
    if (state.points("Approve").value == "yes") {
      Seq(ArrowSuccess)
    } else {
      Seq(Arrow("V4", Some("E7")))
    }
  }


  def Divination(cmd: CommandAutoTask): Future[Map[String, String]] = Future {

    val rate: Double = ((new util.Random).nextInt(10)) / 10.0

    Map("SuccessRate" -> rate.toString)
  }

  def mroute(proxy: ActorRef): Route = get {
    path("kernel") {
      val k = ServiceProxy.idGet(proxy, "hello").map(_.id.toString)
      complete(k)
    }
  }

}
