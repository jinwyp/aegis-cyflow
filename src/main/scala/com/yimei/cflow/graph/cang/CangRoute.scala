package com.yimei.cflow.graph.cang

import akka.actor.ActorRef
import akka.http.scaladsl.server._
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._


import scala.concurrent.duration._

/**
  * Created by hary on 16/12/2.
  */
object CangRoute {

  implicit val flowServiceTimeout = Timeout(2 seconds)

  // todo:
  //  compose all the routes into big one
  // def route(proxy: ActorRef): Route = complete("hello world")
}

