package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.model
import akka.http.scaladsl.model.ContentTypes.`text/html(UTF-8)`
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import com.yimei.cflow.config.FreemarkerConfig

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
      complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(`text/html(UTF-8)`,ByteString(html))))
    }
  }


  def adminLogin: Route = get {
    path("admin2" / "login") {
     val html: String = FreemarkerConfig.render("admin/login.ftl")
      complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(`text/html(UTF-8)`,ByteString(html))))
    }
  }

  def route: Route = cangHtml ~ adminLogin
}

object BasicRoute {
  def apply() = new BasicRoute

  def route(): Route = BasicRoute().route
}

