package com.yimei.cflow

import akka.actor.Props
import akka.http.scaladsl.Http
import com.yimei.cflow.cang.CangSupervisor
import com.yimei.cflow.config.{ApplicationConfig, Core}
import com.yimei.cflow.operation.OperationRoute
import com.yimei.cflow.swagger.{CorsSupport, SwaggerDocService, SwaggerService}
import com.yimei.cflow.user.{UserRoute, UserSupervisor}
import com.yimei.cflow.ying.YingSupervisor


/**
  * Created by hary on 16/12/2.
  */
object FlowApplication extends App with ApplicationConfig with CorsSupport with Core {

  import akka.http.scaladsl.server.Directives._

  // 仓押与应收
  val ying = system.actorOf(Props[YingSupervisor], "YingSupervisor")
  val cang = system.actorOf(Props[CangSupervisor], "CangSupervisor")

  // 用户, 运营, 资金方
  val user = system.actorOf(Props[UserSupervisor], "UserSupervisor")
  //  val operation = system.actorOf(Props[CangSupervisor], "CangSupervisor")
  //  val capital = system.actorOf(Props[CangSupervisor], "CangSupervisor")

  // 路由, 服务启动
  val routes = UserRoute.route ~
    OperationRoute.route ~
    new SwaggerService().route ~
    corsHandler(new SwaggerDocService(system).routes)
  Http().bindAndHandle(routes, "0.0.0.0", system.settings.config.getInt("http.port"))

}
