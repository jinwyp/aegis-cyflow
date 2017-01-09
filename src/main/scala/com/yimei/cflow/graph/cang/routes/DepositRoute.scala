package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.graph.cang.db.DepositTable

/**
  * Created by xl on 17/1/9.
  */
class DepositRoute extends DepositTable with SprayJsonSupport{
//  def createDeposit: Route = post {
//    path("deposit") & entity(as[])
//  }
}
