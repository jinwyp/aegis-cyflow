package com.yimei.cflow.graph.cang
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.yimei.cflow.graph.cang.routes.{CangFlowRoute, CangUserRoute}
import com.yimei.cflow.graph.cang.config.Config._

/**
  * Created by wangqi on 16/12/26.
  */
object Main extends App {

  var root: Route = pathPrefix("cang") {
    CangFlowRoute.route() ~ CangUserRoute.route()
  }

  println(s"http is listening on ${port}")
  Http().bindAndHandle(root, "0.0.0.0", port)
}
