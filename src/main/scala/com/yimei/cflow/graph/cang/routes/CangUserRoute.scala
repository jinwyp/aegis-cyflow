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
import com.yimei.cflow.graph.cang.models.UserModel.UserData

import scala.concurrent.Future
import com.yimei.cflow.graph.cang.services.LoginService._
import com.yimei.cflow.graph.cang.session.{MySession, Session, SessionProtocol}
import spray.json._



/**
  * Created by xl on 16/12/26.
  */
class CangUserRoute extends SprayJsonSupport with ResultProtocol with UserModelProtocol with Session with SessionProtocol {

  import com.yimei.cflow.graph.cang.models.UserModel._

  //---------------------------------------管理员 用户操作-----------------------------------
  /*
   * 管理员添加用户
   * url      localhost:9000/api/cang/user
   * method   post
   * body     {"username":"xl","password":"123456","name":"资金方业务","email":"asdf@qq.com","phone":"12345678912","instanceId":"66666666","className":"fundProvider"}
   */
  def adminAddUser: Route = post {
    (path("users") & entity(as[AddUser])) { user =>
      myRequiredSession { s =>
        complete(addUser(user))
      }
    }
  }

  /*
   * 管理员获取所有用户
   * url      http://localhost:9000/api/cang/users?page=2&count=2
   * method   get
   */
  def adminGetAllUserListRoute: Route = get {
    (path("users") & parameter('page.as[Int].?) & parameter('count.as[Int].?) & parameter('username.as[String].?) & parameter('companyName.as[String].?)) { (p, ps, un, cn) =>
      myRequiredSession { s =>
        val page = if (!p.isDefined) 1 else p.get
        val pageSize = if (!ps.isDefined) 10 else ps.get
        val dynamicquery = DynamicQueryUser(un, cn)
        complete(adminGetAllUser(page, pageSize, dynamicquery))
      }
    }
  }

  /*
   * 管理员获取特定用户
   * url      http://localhost:9000/api/cang/users/:username
   * method   get
   */
  def adminGetSpecificUserRoute: Route = get {
    path("users" / Segment) { (username) =>
      myRequiredSession { s =>
        import scala.concurrent.ExecutionContext.Implicits.global
        val result = for {
          info <- getUserInfoByUsername(username)
        } yield Result(data = Some(info))
        complete(result)
      }
    }
  }

  /*
   * 管理员修改邮箱、电话
   * url      http://localhost:9000/cang/users/:username
   * method   put application/json
   * body     {"userid":"00000","username":"u3","password":"654321","name":"admins","email":"654321@12345.com","phone":"13800000003"}
   */
  def adminModifyUserRoute: Route = put {
    path("users" / Segment) { username =>
      entity(as[UpdateUser]) { user =>
        myRequiredSession { s =>
          complete(adminModifyUser(username, user))
        }
      }
    }
  }

  /*
   * 管理员重置用户密码
   * url      http://localhost:9000/api/cang/users/:username/password
   * method   put
   */
  def adminResetUserPasswordRoute: Route = put {
    pathPrefix("users" / Segment / "password") { username =>
      myRequiredSession { s =>
        complete(adminResetUserPassword(username))
      }
    }
  }

  /*
   * 管理员删除用户
   * url      http://localhost:9000/api/cang/users/:username
   * method   delete
   */
  def adminDisableUserRoute: Route = delete {
    pathPrefix("users" / Segment) { username =>
      myRequiredSession { s =>
        complete(adminDisableUser(username))
      }
    }
  }


  //---------------------------------------管理员 公司操作-----------------------------------
  /*
   * 管理员添加公司
   * url      http://localhost:8000/api/cang/companies
   * method   post application/json
   * body     {"companyName":"瑞茂通","partyClass":"trader"}
   */
  def adminAddCompanyRoute: Route = post {
    (path("companies") & entity(as[AddCompany])) { company =>
      myRequiredSession { s =>
        complete(adminAddCompany(company))
      }
    }
  }

  /*
   * 管理员获取所有公司信息
   * url         http://localhost:9000/api/cang/companies?page=1&count=3&companyName=%E6%98%93%E7%85%A4
   * method      get
   * attention   page/count/companyName都不是必填项
   */
  def adminGetAllCompanyRoute: Route = get {
    (path("companies") & parameter('page.as[Int].?) & parameter('count.as[Int].?) & parameter('companyName.as[String].?)) { (p, ps, cn) =>
      myRequiredSession { s =>
        val page = if (!p.isDefined) 1 else p.get
        val pageSize = if (!ps.isDefined) 10 else ps.get
        complete(adminGetAllCompany(page, pageSize, cn))
      }
    }
  }

  /*
   * 管理员获取特定公司信息
   * url         http://localhost:9000/api/cang/company/:partyClass/:instanceId
   * method      get
   */
  def adminGetSpecificCompanyRoute: Route = get {
    path("companies" / Segment / Segment) { (partyClass, instanceId) =>
      myRequiredSession { s =>
        complete(adminGetSpecificCompany(partyClass, instanceId))
      }
    }
  }

  /*
   * 管理员修改公司信息
   * url      localhost:9000/cang/companies/:partyclass/:instanceId
   * method   put
   * body     瑞茂通
   */
  def adminUpdateCompanyRoute: Route = put {
    path("companies" / Segment / Segment) { (party, instanceId) =>
      entity(as[UpdateCompany]) { uc =>
        myRequiredSession { s =>
          complete(adminUpdateCompany(party, instanceId, uc.companyName))
        }
      }
    }
  }

  //---------------------------------------用户 操作-----------------------------------
  /*
   * 用户登录
   * url      http://localhost:9000/cang/auth/login
   * method   post application/json
   * body     {"username":"u3","password":"123456"}
   */
  def loginRoute: Route = post {
    (path("auth" / "login") & entity(as[UserLogin])) { user =>

      onSuccess(getLoginUserInfo(user)) { (info: UserGroupInfo) =>
        val role = if(!info.gid.isDefined || info.gid.get == "1") info.party else info.party + "Accountant"
        val session = MySession(userName = info.userName, userId = info.userId, party = info.party, instanceId = info.instanceId, companyName = info.companyName)
        mySetSession(session) {
          complete(Result[UserData](data = Some(UserData(userId = info.userId, username = info.userName, email = info.email, phone = info.phone, role = role, companyId = info.instanceId, companyName = info.companyName))))
        }
      }
    }
  }

  /*
   * 用户退出登录
   * url      http://localhost:9000/api/cang/auth/logout
   * method   get
   */
  def logoutRoute: Route = get {
    path("auth" / "logout") {
      myRequiredSession { s =>
        myInvalidateSession {
          complete(Result(data = Some("")))
        }
      }
    }
  }

  /*
   * 获取用户信息
   * url      http://localhost:9000/api/cang/sessionuser
   * method   get
   */
  def getInfoRoute: Route = get {
    path("sessionuser") {
      myRequiredSession { s =>
        import scala.concurrent.ExecutionContext.Implicits.global
        val result = for {
          info <- getUserInfoByUsername(s.userName)
        } yield Result(data = Some(info))

        complete(result)
      }
    }
  }

  /*
   * 用户修改密码
   * url      http://localhost:9000/api/cang/sessionuser/password
   * method   put application/json
   * body     {"newPassword":"654321","oldPassword":"123456"}
   */
  def userModifyPasswordRoute: Route = put {
    (path("sessionuser" / "password") & entity(as[UserChangePwd])) { user =>
      myRequiredSession { session =>
        complete(userModifyPassword(session.party, session.instanceId, session.userId, user))
      }
    }
  }

  /*
   * 用户修改邮箱、电话
   * url      http://localhost:9000/api/cang/sessionuser
   * method   put application/json
   * body     {"email":"6789@6789.com","phone":"13800000002"}
   */
  def userModifySelfRoute: Route = put {
    (path("sessionuser") & entity(as[UpdateSelf])) { user =>
      myRequiredSession { s =>
        complete(userModifySelf(s.party, s.instanceId, s.userId, user))
      }
    }
  }

  def route = adminAddUser ~ adminModifyUserRoute ~ userModifySelfRoute ~ loginRoute ~ userModifyPasswordRoute ~
    adminResetUserPasswordRoute ~ adminDisableUserRoute ~ adminAddCompanyRoute ~ adminGetAllCompanyRoute ~ adminUpdateCompanyRoute ~
    adminGetAllUserListRoute ~ adminGetSpecificCompanyRoute ~ getInfoRoute ~ logoutRoute ~ adminGetSpecificUserRoute
}

object CangUserRoute {
  def apply() = new CangUserRoute
  def route(): Route = CangUserRoute().route
}
