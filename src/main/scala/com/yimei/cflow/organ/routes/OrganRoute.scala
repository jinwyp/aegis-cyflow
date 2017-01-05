package com.yimei.cflow.organ.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._

/**
  * Created by wangqi on 17/1/4.
  */
object OrganRoute {
  def route(proxy:ActorRef)  = UserRoute.route(proxy) ~
    GroupRoute.route ~
    PartyRoute.route ~
    InstRoute.route
}
