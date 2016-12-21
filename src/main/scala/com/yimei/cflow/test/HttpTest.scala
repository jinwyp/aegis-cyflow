package com.yimei.cflow.test

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.FlowRegistry
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.http._
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}

/**
  * Created by wangqi on 16/12/21.
  */
object HttpTest extends App with ApplicationConfig{
  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  FlowRegistry.register(YingGraph.flowType, YingGraph)

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names, true), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  //val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000)

  val routes = AdminRoute.route(proxy) ~
    PartyRoute.route ~
    AutoRoute.route(proxy) ~
    FlowRoute.route(proxy) ~
    GroupRoute.route ~
    InstRoute.route ~
    PartyRoute.route ~
    TaskRoute.route(proxy) ~
    UserRoute.route(proxy)



  //implicit val mysystem = coreSystem // @todo fixme
  Http().bindAndHandle(routes, "0.0.0.0", coreConfig.getInt("http.port"))


}
