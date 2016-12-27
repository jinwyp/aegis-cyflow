package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.http.client.AdminClient
import com.yimei.cflow.graph.cang.models.CangFlowModel._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.yimei.cflow.graph.cang.config
import com.yimei.cflow.graph.cang.config.Config._
import spray.json._
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.api.models.database.FlowDBModel.FlowInstanceEntity

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/26.
  * 流程相关路由
  */
class CangFlowRoute extends AdminClient with SprayJsonSupport with ResultProtocol{

  def startFlow = post {
    pathPrefix("startflow"){
      entity(as[StartFlow]) { startFlow =>
        //该用户是否已经存在。如果不存在要自动添加。 //todo 大磊哥

        val create: Future[Result[FlowInstanceEntity]] = createFlow(rzf,startFlow.basicInfo.applyCompanyId.toString,startFlow.basicInfo.applyUserId.toString,flowType,
          Map("initData"->startFlow.toJson.toString,
            "trafficker"->zjfUserId,
            "traffickerFinance"->zjfFinanceId)
        ) map { c=>
          Result(c)
        }

        complete(create)
      }
    }
  }

  def route = startFlow
}

object CangFlowRoute {
  def apply() = new CangFlowRoute
  def route(): Route = CangFlowRoute().route
}
