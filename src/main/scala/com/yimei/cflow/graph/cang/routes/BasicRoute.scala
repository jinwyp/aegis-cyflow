package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.model.ContentTypes.`text/html(UTF-8)`
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import com.yimei.cflow.config.FreemarkerConfig
import com.yimei.cflow.config.FreemarkerConfig._
/**
  * Created by hary on 17/1/3.
  */
class BasicRoute {


  def cangHtml: Route = get {
    path("admin" / "test") {
     // complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(`text/html(UTF-8)`,ByteString(html))))
      ftl("admin/login.ftl")
    }
  }

  /**
    * 后台管理 平台管理首页
    */
  def adminLogin: Route = get {
    path("warehouse"/ "admin" / "login") {
      ftl("admin/login.ftl")
    }
  }

  def route: Route = cangHtml ~ adminLogin
}

object BasicRoute {
  def apply() = new BasicRoute

  def route(): Route = BasicRoute().route
}

