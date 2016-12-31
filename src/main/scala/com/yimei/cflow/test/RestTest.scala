package com.yimei.cflow.test

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.config.FlywayConfig
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.yimei.cflow.organ.routes.{GroupRoute, InstRoute, PartyRoute}

/**
  * Created by xl on 16/12/20.
  */
object RestTest extends App with FlywayConfig {

  import driver.api._
  migrate

  implicit val system = ActorSystem("test")
  implicit val materializer = ActorMaterializer()
  implicit val executionContext = system.dispatcher

  println("server is started in 8080port")
  val route = new PartyRoute().route ~ new GroupRoute().route ~ new InstRoute().route
  Http().bindAndHandle(route, "127.0.0.1", 8080)
}