package com.yimei.cflow.graph.cang.routes

import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.util.DBUtils._

import scala.concurrent.Future
import com.yimei.cflow.graph.cang.services.LoginService._

/**
  * Created by xl on 16/12/26.
  */
class CangUserRoute {

  // http://127.0.0.1:9001/cang/user/:user_id/:company_Id/:company_Name
  def financeSideEnterRoute: Route = get {
    pathPrefix("user" / Segment / Segment / Segment) { (pc, ii, pn) =>
      complete(financeSideEnter(pc, ii, pn))
    }
  }

  def route = financeSideEnterRoute
}

object CangUserRoute {
  def apply() = new CangUserRoute
  def route(): Route = CangUserRoute().route
}
