package com.yimei.cflow

import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.http.UserRoute
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.util.QueryActor


/**
  * Created by hary on 16/12/2.
  */
object FlowApplication extends App with ApplicationConfig with CorsSupport {

  val names = Array(module_data, module_user, module_flow)

  // daemon master and
  val daemon = coreSystem.actorOf(DaemonMaster.props(names, persist = false), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")

  // 测试用的actor
  val queryActor = coreSystem.actorOf(Props(new QueryActor(daemon)), "queryActor")

  // http
  val routes: Route = UserRoute.route(proxy) ~
      new SwaggerService().route ~
      corsHandler(new SwaggerDocService(coreSystem).routes)

  implicit val mysystem = coreSystem // @todo fixme
  Http().bindAndHandle(routes, "0.0.0.0", config.getInt("http.port"))
}
