package com.yimei.cflow

import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.auto.AutoRegistry
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.FlowRegistry
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.http.{FlowRoute, TaskRoute, UserRoute}
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}


/**
  * Created by hary on 16/12/2.
  */
object FlowApplication extends App with ApplicationConfig with CorsSupport {

  // 1> 注册流程类型
  FlowRegistry.register(flow_ying, YingGraph)

  // 2> 注册自动任务
  // AutoRegistry.register(data_X, (modules: Map[String, ActorRef]) => new Actor {})

  val names = Array(module_auto, module_user, module_group, module_flow, module_id)

  // daemon master and
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  Thread.sleep(1000)


  // 测试Id
  val f = ServiceProxy.idGet(proxy, "hello")
  f.onSuccess {
    case s => println(s"got $s")
  }

  // 测试group服务
  for {
    gc <- ServiceProxy.groupCreate(proxy, "operation", "risk")
  } {
    ServiceProxy.groupTask(proxy, "operation", "risk", "flowId", "helloTask")
    Thread.sleep(1000)

    for {
      gq <- ServiceProxy.groupQuery(proxy, "operation", "risk")
      uc <- ServiceProxy.userCreate(proxy, "operation", "hary")
      claim <- ServiceProxy.groupClaim(proxy, "operation", "risk", "hary", gq.tasks.head._1)
    } {
      println(s"gcreate create = $gc")
      println(s"gquery         = $gq")
      println(s"ucreate        = $uc")
      println(s"claim          = $claim")
      Thread.sleep(1000)
      for{
       uq <- ServiceProxy.userQuery(proxy, "operation", "hary")
      } {
        println(s"userQuery       = $uq")
      }
    }
  }

  Thread.sleep(2000)
  for{
    uq <- ServiceProxy.userQuery(proxy, "operation", "hary")
  } {
    println(s"userQuery       = $uq")
  }


  // http
  val routes: Route =
    FlowRoute.route(proxy) ~
    UserRoute.route(proxy) ~
 //   GroupRoute.route(proxy) ~
    TaskRoute.route(proxy) ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(coreSystem).routes)

  implicit val mysystem = coreSystem // @todo fixme
  Http().bindAndHandle(routes, "0.0.0.0", config.getInt("http.port"))
}
