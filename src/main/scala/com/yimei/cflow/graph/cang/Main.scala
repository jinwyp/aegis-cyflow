package com.yimei.cflow.graph.cang
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.config.ExceptionHandle
import com.yimei.cflow.graph.cang.routes.{CangFlowRoute, CangUserRoute, SessionDemoRoute}

/**
  * Created by wangqi on 16/12/26.
  */
object Main extends App with ExceptionHandle with Config{

  var root: Route = pathPrefix("cang") {
    CangFlowRoute.route() ~ CangUserRoute.route() ~
      SessionDemoRoute.route()
  }

  println(s"http is listening on ${port}")
  Http().bindAndHandle(root, "0.0.0.0", port)
}
