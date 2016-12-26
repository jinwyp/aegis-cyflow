package com.yimei.cflow.graph.cang
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.yimei.cflow.graph.cang.routes.CangFlowRoute

/**
  * Created by wangqi on 16/12/26.
  */
object Main extends App {

  implicit val coreSystem = ActorSystem("ClientSystem")
  implicit val coreExecutor = coreSystem.dispatcher
  implicit val coreMaterializer = ActorMaterializer()
  val coreConfig = coreSystem.settings.config

  var root: Route = pathPrefix("cang") {
    CangFlowRoute.route()
  }


  println(s"http is listening on ${coreConfig.getInt("client.port")}")
  Http().bindAndHandle(root, "0.0.0.0", coreConfig.getInt("client.port"))
}
