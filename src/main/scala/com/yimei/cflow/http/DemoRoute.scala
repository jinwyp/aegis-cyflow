package com.yimei.cflow.http

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/7.
  */
class DemoRoute(proxy: ActorRef) {

  def getDemo: Route = get {
    complete("hello demo")
  }

  def route: Route = getDemo
}

object DemoRoute {
  implicit val userServiceTimeout = Timeout(2 seconds)

  def apply(proxy: ActorRef) = new DemoRoute(proxy)

  def route(proxy: ActorRef): Route = DemoRoute(proxy).route

}
