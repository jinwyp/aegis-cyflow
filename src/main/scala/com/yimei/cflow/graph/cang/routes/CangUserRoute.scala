package com.yimei.cflow.graph.cang.routes

import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.http.models.ResultModel.{Result, ResultProtocol}
import com.yimei.cflow.api.http.models.UserModel._
import com.yimei.cflow.api.util.DBUtils
import DBUtils._

import scala.concurrent.Future
import com.yimei.cflow.graph.cang.services.LoginService._
import com.yimei.cflow.graph.cang.session.{MySession, Session, SessionProtocol}
import spray.json._


/**
  * Created by xl on 16/12/26.
  */
class CangUserRoute extends SprayJsonSupport with ResultProtocol with UserModelProtocol with Session with SessionProtocol {

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

  def adminModifyUserRoute: Route = post {
    pathPrefix("amu" / Segment / Segment) { (party, instance_id) =>
      entity(as[UpdateUser]) { user =>
        complete(adminModifyUser(party, instance_id, user))
      }
    }
  }

  def userModifySelfRoute: Route = post {
    (pathPrefix("umu") & entity(as[UpdateSelf])) { user =>
      //session校验 todo
      //party和instance_id应该从session里面 todo
      val party = "financer"
      val instance_id = "444"
      val userId = "333"
      complete(userModifySelf(party, instance_id, userId, user))
    }
  }

  def loginRoute: Route = post {
    (pathPrefix("login") & entity(as[UserLogin])) { user =>
      import scala.concurrent.ExecutionContext.Implicits.global
      val result = for {
        s <- getLoginUserInfo(user)
      } yield {
        mySetSession(s)
        Result[MySession](data = Some(s), success = true)
      }
      complete(result)
    }
  }

  def userModifyPasswordRoute: Route = post {
    (pathPrefix("mpw") & entity(as[UserChangePwd])) { user =>
      //需要session校验身份 todo
      //从session中获取party和instance_id todo
      val party = "financer"
      val instance_id = "444"
      val userId = "333"
      complete(userModifyPassword(party, instance_id, userId, user))
    }
  }

  def adminResetUserPasswordRoute: Route = put {
    pathPrefix("rup" / Segment / Segment / Segment) { (party, instance_id, userId) =>
      complete(adminResetUserPassword(party, instance_id, userId))
    }
  }

  def adminGetUserListRoute: Route = get {
    pathPrefix("gul" / Segment / Segment) { (party, instance_id) =>
      (parameter('limit.as[Int]) & parameter('offset.as[Int])) { (limit, offset) =>
        complete(adminGetUserList(party, instance_id, limit, offset))
      }
    }
  }

  def route = financeSideEnterRoute ~ addInvestorRoute ~ adminModifyUserRoute ~ userModifySelfRoute ~ loginRoute ~ userModifyPasswordRoute ~
    adminResetUserPasswordRoute ~ adminGetUserListRoute
}

object CangUserRoute {
  def apply() = new CangUserRoute
  def route(): Route = CangUserRoute().route
}
