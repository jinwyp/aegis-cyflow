package com.yimei.zflow.single

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.XieJieTestRoute
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.config.{ApplicationConfig, MyExceptionHandler}
import com.yimei.cflow.engine.graph.GraphLoader
import com.yimei.cflow.engine.routes.EditorRoute
import com.yimei.cflow.engine.{DaemonMaster, EngineRoute, FlowRegistry}
import com.yimei.cflow.graph.cang.routes._
import com.yimei.cflow.http.ResourceRoute
import com.yimei.cflow.organ.routes.OrganRoute
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.util.TestClient

/**
  * Created by hary on 17/1/6.
  */

object FlowApp extends App with ApplicationConfig with CorsSupport with MyExceptionHandler {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  drop
  migrate

  GraphLoader.loadall()

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy: ActorRef = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000);

  //  var root: Route = pathPrefix("cang") {
  //    CangFlowRoute.route() ~
  //      CangUserRoute.route() ~
  //      SessionDemoRoute.route()
  //  }

  // 3> http
  val base: Route = pathPrefix("api") {
    OrganRoute.route(proxy) ~
      EngineRoute.route(proxy)
  } ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(coreSystem).routes) ~
    ResourceRoute.route(proxy) ~
    EditorRoute.route(proxy) ~
    pathPrefix("api" / "cang") {
      CangFlowRoute.route() ~
        CangUserRoute.route() ~
        SessionDemoRoute.route()
    } ~
    CangStatic.route() ~
    BasicRoute.route() ~
    XieJieTestRoute().route

  def |+|(left: Route, right: Route) = left ~ right

  val empty: Route = get {
    path("impossible") {
      complete("impossible")
    }
  }

  val flowRoute = FlowRegistry.registries.map { entry =>
    pathPrefix(entry._1) {
      entry._2.routes.map(_ (proxy)).foldLeft(empty)(|+|)
    }
  }.foldLeft(empty)(|+|)

  val all = logRequest("debug") {
    base ~ flowRoute
  }

  implicit val mySystem = coreSystem // @todo fixme

  println(s"http is listening on ${coreConfig.getInt("http.port")}")
  Http().bindAndHandle(all, "0.0.0.0", coreConfig.getInt("http.port"))
}


