package com.yimei.cflow.engine.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.asset.AssetRoute
import com.yimei.cflow.http.{AdminRoute, TaskRoute}

/**
  * Created by wangqi on 17/1/4.
  */
object EngineRoute {
  def route(proxy: ActorRef) = AdminRoute.route(proxy) ~
    AssetRoute.route ~
    TaskRoute.route(proxy) ~
    AutoRoute.route(proxy)

}
