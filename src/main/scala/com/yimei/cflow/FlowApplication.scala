package com.yimei.cflow

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.Http
import com.yimei.cflow.cang.CangFlowMaster
import com.yimei.cflow.config.{ApplicationConfig, Core}
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.integration.DaemonMaster
import com.yimei.cflow.operation.OperationRoute
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.user.{UserMaster, UserRoute}
import com.yimei.cflow.ying.YingFlowMaster


/**
  * Created by hary on 16/12/2.
  */
object FlowApplication extends App with ApplicationConfig with CorsSupport {

  // 仓押, 应收, 数据, 用户 4大模块
  var moduleProps = Map[String, Props](
    module_ying -> YingFlowMaster.props(),
    module_cang -> CangFlowMaster.props(),
    module_data -> DataMaster.props(),
    module_user -> UserMaster.props()
  )

  // 启动daemon master
  system.actorOf(DaemonMaster.props(moduleProps), "DaemonMaster")

  // 路由, 服务启动
  import akka.http.scaladsl.server.Directives._

  // route assembly
  val routes = UserRoute.route ~
    OperationRoute.route ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(system).routes)

  // start server
  Http().bindAndHandle(routes, "0.0.0.0", system.settings.config.getInt("http.port"))
}
