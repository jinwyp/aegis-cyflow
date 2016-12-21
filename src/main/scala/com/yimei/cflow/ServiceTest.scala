package com.yimei.cflow

import akka.actor.Props
import akka.http.scaladsl.Http
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.FlowRegistry
import com.yimei.cflow.graph.cang.CangRoute
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.http._
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.util.{TestClient, TestUtil}

/**
  * Created by hary on 16/12/3.
  */
object ServiceTest extends App with ApplicationConfig with CorsSupport {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor


  FlowRegistry.register(YingGraph.flowType, YingGraph)

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000)

  //  TestUtil.test(proxy, client, "00", "hary","fund","wangqiId","fund","wqGroup")

  for (i <- 1 to 1) {
    TestUtil.test("ying", proxy, client, s"00$i", s"hary$i", s"fund$i", s"wangqiId$i", s"fund$i", s"wqGroup$i")
  }

  // 3> http
  val routes: Route = FlowRoute.route(proxy) ~
    UserRoute.route(proxy) ~
    GroupRoute.route ~
    TaskRoute.route(proxy) ~
    AutoRoute.route(proxy) ~
    CangRoute.route(proxy) ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(coreSystem).routes)

  implicit val mysystem = coreSystem // @todo fixme
  Http().bindAndHandle(routes, "0.0.0.0", coreConfig.getInt("http.port"))

}

