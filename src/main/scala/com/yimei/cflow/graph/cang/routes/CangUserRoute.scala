package com.yimei.cflow.graph.cang.routes

import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.http.models.ResultModel.ResultProtocol
import com.yimei.cflow.api.http.models.UserModel._

import com.yimei.cflow.util.DBUtils._

import scala.concurrent.Future
import com.yimei.cflow.graph.cang.services.LoginService._
import com.yimei.cflow.graph.cang.session.Session
import spray.json._


/**
  * Created by xl on 16/12/26.
  */
class CangUserRoute extends SprayJsonSupport with ResultProtocol with UserModelProtocol with Session {

  import com.yimei.cflow.graph.cang.models.UserModel._

  // http://127.0.0.1:9001/cang/fse/:user_id/:company_Id body:companyName post
  def financeSideEnterRoute: Route = post {
    pathPrefix("fse" / Segment / Segment ) { (ui, ci) =>
      entity(as[AddUser]) { user =>
        complete(financeSideEnter(ui, ci, user))
      }
    }
  }

  def addInvestorRoute: Route = post {
    pathPrefix("user" / Segment / Segment ) { (instance_id, userId) =>
      entity(as[AddUser]) { user =>
//        val userInfo = UserInfo(user.password, Some(user.phone), Some(user.email), user.name)
        complete(addInvestor(instance_id, userId, user))
      }

    }
  }

//  def createUserRoute: Route = post {
//    pathPrefix("user" / Segment / Segment / Segment) { (party, instance_id, userId) =>
//      entity(as[AddUser]) { user =>
//
//        complete("us")
////        val userInfo = UserInfo(password = user)
////        complete(createUser(party, instance_id, userId, ))
//        }
//
//    }
//  }





  def route = financeSideEnterRoute ~ addInvestorRoute
}

object CangUserRoute {
  def apply() = new CangUserRoute
  def route(): Route = CangUserRoute().route
}
