package com.yimei.cflow

import java.util.UUID

import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import Directives._
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.user.UserRoute
import com.yimei.cflow.util.{QueryActor, QueryTest}
import com.yimei.cflow.config.GlobalConfig._

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/3.
  */
object EngineTest extends App with ApplicationConfig with CorsSupport {


  // daemon master and
  val names = Array(module_data, module_user, module_engine)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")

  // 测试用的actor
  val queryActor = coreSystem.actorOf(Props(new QueryActor(daemon)), "queryActor")

  // route assembly
  val routes: Route = UserRoute.route(proxy) ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(coreSystem).routes)
  implicit val mysystem = coreSystem  // @todo fixme
  Http().bindAndHandle(routes, "0.0.0.0", config.getInt("http.port"))


  val flowId = UUID.randomUUID().toString
  coreSystem.scheduler.scheduleOnce(1 seconds, queryActor, QueryTest(module_engine, flowId, "hary"))
}




