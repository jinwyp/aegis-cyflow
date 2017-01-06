package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.FreemarkerConfig._

/**
  * Created by hary on 17/1/3.
  */
class BasicRoute {


  def cangHtml: Route = get {
    path("admin" / "test") {
      // complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(`text/html(UTF-8)`,ByteString(html))))
      ftl("admin/login.ftl")
    }
  }

  /**
    * 后台管理 平台管理首页
    */
  def adminLogin: Route = get {
    path("warehouse" / "admin" / "login") {
      ftl("admin/login.ftl")
    }
  }

  /**
    * 管理后台平台管理员添加的个人信息 - 基本信息
    *
    * @return
    */
  def adminCurrentUserInfo = get {
    path("warehouse" / "admin" / "home" / "session" / "info") {
      ftl("admin/platform/sessionInfo.ftl")
    }
  }


  /**
    * 管理后台首页跳转
    *
    * @return
    */
  def adminIndexRedirect = get {
    path("warehouse" / "admin") {
      redirect("/warehouse/admin/home", StatusCodes.PermanentRedirect)
    }
  }


  /**
    * 管理后台平台管理员范例页面 - Dashboard
    *
    * @return
    */
  def adminDemoDashboard = get {
    path("warehouse" / "admin" / "home" / "demo" / "dashboard") {
      ftl("admin/platform/demoDashboard.ftl")
    }
  }

  /**
    * 管理后台平台管理员范例页面 - Echart
    *
    * @return
    */
  def adminDemoEchart = get {
    path("warehouse" / "admin" / "home" / "demo" / "echart") {
      ftl("admin/platform/demoEchart.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 用户管理 - 用户列表
    *
    * @return
    */
  def adminUserList = get {
    path("warehouse" / "admin" / "home" / "userlist") {
      ftl("admin/platform/userList.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 用户管理 - 添加用户
    *
    * @return
    */
  def adminUserAdd = get {
    path("warehouse" / "admin" / "home" / "user" / "add") {
      ftl("admin/platform/userInfo.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 用户管理 - 编辑用户
    *
    * @return
    */
  def adminUserEdit = get {
    path("warehouse" / "admin" / "home" / "user" / Segment / "edit") { userId =>
      ftl("admin/platform/userInfo.ftl")
    }
  }

  /**
    * 管理后台平台管理员 - 用户管理 - 用户信息
    *
    * @return
    */
  def adminUserInfo = get {
    path("warehouse" / "admin" / "home" / "user" / Segment) { userId =>
      ftl("admin/platform/userInfo.ftl")
    }
  }


  /**
    * 管理后台平台管理员添加的个人信息 - 修改密码
    *
    * @return
    */
  def adminCurrentUserModifyPassport = get {
    path("warehouse" / "admin" / "home" / "session" / "password") {
      ftl("admin/platform/sessionModifyPassword.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 融资管理 - 订单列表
    *
    * @return
    */
  def adminFinanceOrderDetails = get {
    path("warehouse" / "admin" / "home" / "finance") {
      ftl("admin/customer/financeOrderList.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 融资管理 - 订单合同上传
    *
    * @return
    */
  def adminCompanyList = get {
    path("warehouse" / "admin" / "home" / "companylist") {
      ftl("admin/platform/companyList.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 用户管理 - 添加公司
    *
    * @return
    */
  def adminCompanyAdd = get {
    path("warehouse" / "admin" / "home" / "company" / "add") {
      ftl("admin/platform/companyInfo.ftl")
    }
  }


  /**
    * 管理后台平台管理员 - 用户管理 - 编辑公司
    *
    * @return
    */
  def adminCompanyEdit = get {
    path("warehouse" / "admin" / "home" / "company" / Segment / "edit") { companyId =>
      ftl("admin/platform/companyInfo.ftl")
    }
  }

  /**
    * 管理后台平台管理员 - 用户管理 - 公司信息
    *
    * @return
    */
  def adminCompanyInfo = get {
    path("warehouse" / "admin" / "home" / "company" / Segment) { companyId =>
      ftl("admin/platform/companyInfo.ftl")
    }
  }


  /**
    * 管理后台平台管理员首页
    *
    * @return
    */
  def adminHome = get {
    pathPrefix("warehouse" / "admin" / "home") {
      redirect("/warehouse/admin/home/session/info", StatusCodes.PermanentRedirect)
    }
  }


  /////////////////////////////////////////////
  def contractJindiao = get {
    pathPrefix("warehouse" / "admin" / "home" / "finance" / "contract" /Segment / "jindiao") { id =>
      ftl("admin/customer/hello.ftl")
    }
  }

  def contractJianguan = get {
    pathPrefix("warehouse" / "admin" / "home" / "finance" / "contract" /Segment / "jianguan") { id =>
      ftl("admin/customer/hello.ftl")
    }
  }


  def route: Route = contractJindiao ~ contractJianguan ~ cangHtml ~ adminLogin ~ adminCurrentUserInfo ~
    adminDemoDashboard ~ adminDemoEchart ~
    adminUserList ~ adminUserAdd ~ adminUserEdit ~ adminUserInfo ~
    adminCurrentUserModifyPassport ~ adminFinanceOrderDetails ~
    adminCompanyList ~ adminCompanyAdd ~ adminCompanyEdit ~ adminCompanyInfo ~
    adminHome ~ adminIndexRedirect
}

object BasicRoute {
  def apply() = new BasicRoute

  def route(): Route = BasicRoute().route
}

