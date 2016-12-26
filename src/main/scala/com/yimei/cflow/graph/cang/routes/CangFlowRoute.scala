package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.http.client.AdminClient
import com.yimei.cflow.graph.cang.models.CangFlowModel._
/**
  * Created by wangqi on 16/12/26.
  * 流程相关路由
  */
class CangFlowRoute extends AdminClient{

//  def startFlow = post {
//    pathPrefix("startflow"){
//      entity(as[StartFlow]) { startFlow =>
//        //该用户是否已经存在。如果不存在要自动添加。 //todo 大磊哥
//        createFlow("rz",startFlow.basicInfo.applyCompanyId,startFlow.basicInfo.applyUserId,"cang",)
//      }
//    }
//
//  }

  def route = ???
}

object CangFlowRoute {
  def apply() = new CangFlowRoute
  def route(): Route = CangFlowRoute().route
}
