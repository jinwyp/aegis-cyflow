package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.http.client.AdminClient
import com.yimei.cflow.graph.cang.models.CangFlowModel._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.yimei.cflow.api.http.models.AdminModel.{AdminProtocol, HijackEntity}
import com.yimei.cflow.graph.cang.config.Config
import spray.json._
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.api.models.database.FlowDBModel.FlowInstanceEntity
import com.yimei.cflow.api.models.flow.State
import com.yimei.cflow.api.util.HttpUtil.request
import com.yimei.cflow.config.FreemarkerConfig
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.services.FlowService._

import scala.concurrent.Future

/**
  * Created by hary on 17/1/3.
  */
class BasicRoute {

  val html =
    """
      |<html>
      |
      |
      |</html>
    """.stripMargin

  def adminLogin: Route = get {
    path("/warehouse/admin/login") {
      complete(FreemarkerConfig.render("login"))
    }
  }

  def kkk: Route = ???

  def route: Route = ???
}

object BasicRoute {
  def apply() = new BasicRoute




  def route(): Route = BasicRoute().route
}

