package com.yimei.cflow

import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.{DaemonMaster, FlowRegistry, GraphLoader}
import com.yimei.cflow.graph.cang.CangRoute
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.http._
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.util.{TestClient, TestUtil}

/**
  * Created by hary on 16/12/3.
  */
object ServiceTest extends App with ApplicationConfig with CorsSupport {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  migrate

  //FlowRegistry.register(YingGraph.flowType, YingGraph)
  GraphLoader.loadall()

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000);

  // 3> http
  val base: Route = pathPrefix("api") {
    AdminRoute.route(proxy) ~
      UserRoute.route(proxy) ~
      GroupRoute.route ~
      TaskRoute.route(proxy) ~
      AutoRoute.route(proxy) ~
      PartyRoute.route ~
      InstRoute.route ~
      new SwaggerService().route ~
      corsHandler(new SwaggerDocService(coreSystem).routes)
  } ~
    ResourceRoute.route(proxy)

  def |+|(left: Route, right: Route) = left ~ right
  val empty: Route = get { path("impossible") {complete("impossible")}}

  val flowRoute = FlowRegistry.registries.map { entry =>
   pathPrefix(entry._1) {
     entry._2.routes.foldLeft(empty)(|+|)
   }
  }.foldLeft(empty)(|+|)

  val all = base ~ flowRoute

  implicit val mysystem = coreSystem // @todo fixme

  println(s"http is listening on ${coreConfig.getInt("http.port")}")
  Http().bindAndHandle(all, "0.0.0.0", coreConfig.getInt("http.port"))
}

