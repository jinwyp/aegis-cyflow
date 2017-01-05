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
import akka.http.scaladsl.model.headers.HttpOriginRange.*
import akka.http.scaladsl.model.headers.{HttpOrigin, HttpOriginRange}

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

  /*
   * 管理员添加公司
   * url      http://localhost:9000/api/cang/users?page=2&pageSize=2
   * method   get
   */
  def adminGetAllUserListRoute: Route = get {
    (path("users") & parameter('page.as[Int]) & parameter('pageSize.as[Int])) { (page, pageSize) =>
      complete(adminGetAllUser(page, pageSize))
    }
  }

  /*
   * 管理员添加公司
   * url      http://localhost:8000/api/cang/companies
   * method   post application/json
   * body     {"companyName":"瑞茂通","partyClass":"trader"}
   */
  def adminAddCompanyRoute: Route = post {
    (path("companies") & entity(as[AddCompany])) { company =>
      complete(adminAddCompany(company))
    }
  }

  /*
   * 管理员获取所有公司
   * url         http://localhost:9000/api/cang/companies?page=1&count=3&companyName=%E6%98%93%E7%85%A4
   * method      get
   * attention   page/count/companyName都不是必填项
   */
  def adminGetAllCompanyRoute: Route = get {
    (path("companies") & parameter('page.as[Int].?) & parameter('count.as[Int].?) & parameter('companyName.as[String].?)) { (p, ps, cn) =>
      val page = if(!p.isDefined) 1 else p.get
      val pageSize = if(!ps.isDefined) 10 else ps.get
      complete(adminGetAllCompany(page, pageSize, cn))
    }
  }

  /*
   * 管理员获取所有公司
   * url      localhost:9000/cang/admin/:partyclass/:instance_id
   * method   put
   * body     瑞茂通
   */
  def adminUpdateCompanyRoute: Route = put {
    path("admin" / "partyClass" / Segment/ "instanceId" / Segment) { (party, instanceId) =>
      entity(as[String]) { companyName =>
        println(companyName + "----------" + party + "---------" + instanceId)
        complete(adminUpdateCompany(party, instanceId, companyName))
      }
    }
  }
  
  /*
   * 管理员修改邮箱、电话
   * url      http://localhost:9000/cang/admin/userinfo/:party/:instance_id
   * method   post application/json
   * body     {"userid":"00000","username":"u3","password":"654321","name":"admins","email":"654321@12345.com","phone":"13800000003"}
   */
  def adminModifyUserRoute: Route = post {
    path("admin" / "userinfo" / Segment / Segment) { (party, instance_id) =>
      entity(as[UpdateUser]) { user =>
        complete(adminModifyUser(party, instance_id, user))
      }
    }
  }

  /*
   * 用户修改邮箱、电话
   * url      http://localhost:9000/cang/user/info
   * method   post application/json
   * body     {"email":"6789@6789.com","phone":"13800000002"}
   */
  def userModifySelfRoute: Route = post {
    (path("user" / "info") & entity(as[UpdateSelf])) { user =>
      myRequiredSession { session =>
        complete(userModifySelf(session.party, session.instanceId, session.userId, user))
      }
    }
  }

  /*
   * 用户登录
   * url      http://localhost:9000/cang/auth/login
   * method   post application/json
   * body     {"username":"u3","password":"123456"}
   */
  def loginRoute: Route = post {
    (path("auth" / "login") & entity(as[UserLogin])) { user =>
      import scala.concurrent.ExecutionContext.Implicits.global
      onSuccess(getLoginUserInfo(user)) { s =>
        mySetSession(s) {
          complete(Result[LoginRespModel](data = Some(LoginRespModel(token = s.token, data = UserData(userId = s.userId, username = s.userName, email = s.email, mobilePhone = s.phone, role = s.party, companyId = "", companyName = ""))), success = true))
        }
      }
    }
  }

  /*
   * 用户修改密码
   * url      http://localhost:9000/cang/user/password
   * method   post application/json
   * body     {"newPassword":"654321","oldPassword":"123456"}
   */
  def userModifyPasswordRoute: Route = post {
    (path("user" / "password") & entity(as[UserChangePwd])) { user =>
      myRequiredSession { session =>
        complete(userModifyPassword(session.party, session.instanceId, session.userId, user))
      }
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

  def adminDisableUserRoute: Route = get {
    pathPrefix("adu" / Segment) { userId =>
      complete(adminDisableUser(userId))
    }
  }

  def route = financeSideEnterRoute ~ addInvestorRoute ~ adminModifyUserRoute ~ userModifySelfRoute ~ loginRoute ~ userModifyPasswordRoute ~
    adminResetUserPasswordRoute ~ adminGetUserListRoute ~ adminDisableUserRoute ~ adminAddCompanyRoute ~ adminGetAllCompanyRoute ~ adminUpdateCompanyRoute ~
    adminGetAllUserListRoute
}

object CangUserRoute {
  def apply() = new CangUserRoute
  def route(): Route = CangUserRoute().route
}
