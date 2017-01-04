package com.yimei.cflow.test

import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.config.{ApplicationConfig, FlywayConfig}
import com.yimei.cflow.engine.graph.GraphLoader
import com.yimei.cflow.engine.routes.AutoRoute
import com.yimei.cflow.engine.{DaemonMaster, FlowRegistry}
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.http.{ResourceRoute, _}
import com.yimei.cflow.organ.routes._

/**
  * Created by wangqi on 16/12/21.
  */
object HttpTest extends App with ApplicationConfig with FlywayConfig {
  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor
  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)
  migrate

  //FlowRegistry.register(YingGraph.flowType, YingGraph)
  GraphLoader.loadall()

  //FlowRegistry.registries("ying").inEdges.foreach(t=>log.info("{}:{}",t._1,t._2.foreach(t=>print(t))))

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  //val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000)

  val routes = AdminRoute.route(proxy) ~
    PartyRoute.route ~
    AutoRoute.route(proxy) ~
    //FlowRoute.route(proxy) ~
    GroupRoute.route ~
    InstRoute.route ~
    PartyRoute.route ~
    TaskRoute.route(proxy) ~
    UserRoute.route(proxy) ~
    ResourceRoute.route(proxy)




  //implicit val mysystem = coreSystem // @todo fixme
  println(s"i am listening ad ${coreConfig.getInt("http.port")}")
  Http().bindAndHandle(routes, "0.0.0.0", coreConfig.getInt("http.port"))
}
