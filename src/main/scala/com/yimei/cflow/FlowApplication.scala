package com.yimei.cflow

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.yimei.cflow.cang.CangSupervisor
import com.yimei.cflow.operation.OperationRoute
import com.yimei.cflow.user.{UserRoute, UserSupervisor}
import com.yimei.cflow.ying.YingSupervisor

import concurrent.duration._


/**
  * Created by hary on 16/12/2.
  */
object FlowApplication extends App {

  import akka.http.scaladsl.server.Directives._

  implicit val system = ActorSystem("RiskSystem")
  implicit val timeout = Timeout(5 seconds)
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  // 仓押与应收
  val ying = system.actorOf(Props[YingSupervisor], "YingSupervisor")
  val cang = system.actorOf(Props[CangSupervisor], "CangSupervisor")

  // 用户, 运营, 资金方
  val user = system.actorOf(Props[UserSupervisor], "UserSupervisor")
//  val operation = system.actorOf(Props[CangSupervisor], "CangSupervisor")
//  val capital = system.actorOf(Props[CangSupervisor], "CangSupervisor")

  // 路由, 服务启动
  val routes = UserRoute.route ~  OperationRoute.route
  Http().bindAndHandle(routes, "0.0.0.0", system.settings.config.getInt("http.port"))

}
