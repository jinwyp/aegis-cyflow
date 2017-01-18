package com.yimei.cflow.engine.routes

//import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._

/**
  * Created by hary on 16/12/6.
  */



//@Path("/auto?flowId=:flowId&taskName=:taskName")
class AutoRoute(proxy: ActorRef) {

  def getData = path("data") {
      parameters('flowId, 'taskName) { (flowId, taskName) =>
        complete(s"$taskName + $flowId")
    }
  }

  def route: Route = getData
}


/**
  * Created by hary on 16/12/2.
  */
object AutoRoute {
  def apply(proxy: ActorRef) = new AutoRoute(proxy)
  def route(proxy: ActorRef): Route = AutoRoute(proxy).route
}


