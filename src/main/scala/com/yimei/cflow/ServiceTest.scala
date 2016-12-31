package com.yimei.cflow

import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.{ApplicationConfig, MyExceptionHandler}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.graph.GraphLoader
import com.yimei.cflow.engine.routes.{EditorRoute, ResourceRoute}
import com.yimei.cflow.engine.{DaemonMaster, FlowRegistry}
import com.yimei.cflow.graph.cang.routes.{CangFlowRoute, CangUserRoute, SessionDemoRoute}
import com.yimei.cflow.http._
import com.yimei.cflow.organ.routes._
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.util.TestClient

/**
  * Created by hary on 16/12/3.
  */
object ServiceTest extends App with ApplicationConfig with CorsSupport with MyExceptionHandler {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  drop
  migrate

  GraphLoader.loadall()

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000);

//  var root: Route = pathPrefix("cang") {
//    CangFlowRoute.route() ~
//      CangUserRoute.route() ~
//      SessionDemoRoute.route()
//  }

  // 3> http
  val base: Route = pathPrefix("api") {
    AdminRoute.route(proxy) ~
      UserRoute.route(proxy) ~
      GroupRoute.route ~
      FileRoute().route ~
      TaskRoute.route(proxy) ~
      AutoRoute.route(proxy) ~
      PartyRoute.route ~
      InstRoute.route ~
      new SwaggerService().route ~
      corsHandler(new SwaggerDocService(coreSystem).routes)
  } ~
    ResourceRoute.route(proxy) ~
    EditorRoute.route(proxy) ~
    pathPrefix("cang") {
      CangFlowRoute.route() ~
        CangUserRoute.route() ~
        SessionDemoRoute.route()
    }
  XieJieTestRoute().route

  def |+|(left: Route, right: Route) = left ~ right
  val empty: Route = get { path("impossible") {complete("impossible")}}

  val flowRoute = FlowRegistry.registries.map { entry =>
   pathPrefix(entry._1) {
     entry._2.routes.map(_(proxy)).foldLeft(empty)(|+|)
   }
  }.foldLeft(empty)(|+|)

  val all = base ~ flowRoute

  implicit val mySystem = coreSystem // @todo fixme

  println(s"http is listening on ${coreConfig.getInt("http.port")}")
  Http().bindAndHandle(all, "0.0.0.0", coreConfig.getInt("http.port"))
}

