package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.model
import model.ContentTypes.`text/html(UTF-8)`
import model.ContentType.`; charset=`
import com.yimei.cflow.api.http.client.AdminClient
import com.yimei.cflow.graph.cang.models.CangFlowModel._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.{HttpOrigin, Origin, RawHeader}
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
import akka.http.scaladsl.model.MediaTypes.`text/html`
import akka.util.ByteString
import akka.http.scaladsl.model.headers.`Content-Type`


import scala.concurrent.Future

/**
  * Created by hary on 17/1/3.
  */
class BasicRoute {

  val html =
    """
      |<!DOCTYPE html>
      |<html>
      |<title>hello titel</title>
      |
      |<body>
      |<p> hello </p>
      |<p> hello </p>
      |<p> hello </p>
      |<p> hello </p>
      |<p> hello </p>
      |</body>
      |
      |</html>
    """.stripMargin


  def cangHtml: Route = get {
    path("admin" / "test") {
      respondWithDefaultHeader(RawHeader("Content-Type", "text/html;charset=UTF-8")) {
        complete(html)
        //     complete(StatusCodes.OK, List(`Content-Type`(`text/html(UTF-8)`)), html)
        //      complete(StatusCodes.OK, List(`Content-Type`(`text/html(UTF-8)`)), html)
        //      complete("ok")
      }
    }
  }


  def adminLogin: Route = get {
    path("admin" / "login") {
      complete(FreemarkerConfig.render("admin/login.ftl"))
    }
  }

  def route: Route = cangHtml ~ adminLogin
}

object BasicRoute {
  def apply() = new BasicRoute

  def route(): Route = BasicRoute().route
}

