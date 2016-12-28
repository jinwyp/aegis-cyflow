package com.yimei.cflow.http

import akka.actor.ActorRef
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import akka.util.Timeout
import com.yimei.cflow.config.ApplicationConfig

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/22.
  */
class ResourceRoute(proxy: ActorRef) extends ApplicationConfig {
  def route: Route = getFromDirectory(coreConfig.getString("management.dir"))

}

/**
  * Created by hary on 16/12/2.
  */
object ResourceRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new ResourceRoute(proxy)

  def route(proxy: ActorRef): Route = ResourceRoute(proxy).route

}
