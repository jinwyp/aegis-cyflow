package com.yimei.cflow.driver

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.routes.AutoRoute
import com.yimei.cflow.engine.{DaemonMaster, FlowRegistry}
import com.yimei.cflow.graph.cang.CangRoute
import com.yimei.cflow.graph.ying.YingConfig._
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.http.{ResourceRoute, _}
import com.yimei.cflow.organ.routes.{GroupRoute, UserRoute}
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}


/**
  * Created by hary on 16/12/2.
  */
object FlowApplication extends App with ApplicationConfig with CorsSupport {

  // 1> 注册流程类型
  FlowRegistry.register(flow_ying, YingGraph)

  // 2> 启动服务
  val names = Array(module_auto, module_user, module_group, module_flow, module_id)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  Thread.sleep(1000)

  // 3> http
  val routes: Route = FlowRoute.route(proxy) ~
    UserRoute.route(proxy) ~
    GroupRoute.route ~
    TaskRoute.route(proxy) ~
    AutoRoute.route(proxy) ~
    ResourceRoute.route(proxy) ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(coreSystem).routes)

//  implicit val mysystem = coreSystem // @todo fixme
  Http().bindAndHandle(routes, "0.0.0.0", coreConfig.getInt("http.port"))
}
