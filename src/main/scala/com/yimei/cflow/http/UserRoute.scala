package com.yimei.cflow.http

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.{User, UserProtocol}
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}

import scala.concurrent.Future
import scala.concurrent.duration._

/*

// 参与方类别管理
GET  /party?limit=10&offset=20         参与方类别列表
POST /party/:class                     创建参与方类别
GET  /party/:class                     查询参与方类别
PUT  /party/:class                     更新参与方类别

// 参与方运营组管理
POST   /party/group/:gname              创建参与方运营组
DELETE /party/group/:gname              删除参与方运营组
DELETE /party/group/:gname              更新参与方运营组
GET    /party/group?limit=10&offset=20  参与方运营组列表

// 参与方实例管理
POST /party/:class/:class_id           创建参与方实例
PUT  /party/:class/:class_id           更新参与方实例
GET  /party/:class/:class_id           查询参与方实例

// 参与方用户管理(:class + :class_id = userType)
POST /user/:class/:class_id/:userId                 创建用户
GET  /user/:class/:class_id/:userId                 查询用户  -- 应该拿到: 1. 用户的基本信息, 2. 用户的任务
GET  /user/:class/:class_id?limit=10&offset=20      用户列表  -- 拿到用户的列表信息
PUT  /user/:class/:class_id:/:userId                更新用户  -- 更新用户的基本信息

// 用户任务管理
GET /user/:class/:class_id/:userId/utask?history     查询用户任务, 如果有history参数, 则也包含history信息
PUT /user/:class/:class_id/:userId/utask/:taskId     提交用户提交任务

// 组任务管理
GET /user/:class/:class_id/:userId/gtask           查询用户组任务列表 - 只要是用户所在的组, 任务都查出来
PUT /user/:class/:class_id/:userId/gtask/:taskId   claim任务

*/

@Path("/user/:userId")
class UserRoute(proxy: ActorRef) extends UserProtocol with SprayJsonSupport {

  implicit val timeout = UserRoute.userServiceTimeout // todo  why import User.userServiceTimeout does not work

  /**
    * 创建用户
    */
  @ApiOperation(value = "userState", notes = "", nickname = "创建用户", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "创建用户",
      required = true,
      dataType = "com.yimei.cflow.user.User.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def postUser: Route = post {
    pathPrefix("user" / Segment) { userId =>
      pathEnd {
        parameter('userType) { userType =>
          complete(ServiceProxy.userCreate(proxy, userType, userId))
        }
      }
    }
  }

  // todo 1: add hierachy info support
  // todo 2: idempotent processing in backend

  /**
    * 查询用户
    *
    * @return
    */
  @ApiOperation(value = "userState", notes = "", nickname = "查询用户状态", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "查询用户状态",
      required = true,
      dataType = "com.yimei.cflow.user.User.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getUser: Route = get {
    pathPrefix("user" / Segment) { userId =>
      pathEnd {
        parameter("userType") { userType =>
          complete(ServiceProxy.userQuery(proxy, userType, userId))
        }
      }
    }
  }

  /**
    * 查询用户
    *
    * @return
    */
  @ApiOperation(value = "userState", notes = "", nickname = "查询用户状态", httpMethod = "PUT")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "查询用户状态",
      required = true,
      dataType = "com.yimei.cflow.user.User.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def putUser: Route = put {
    pathPrefix("user" / Segment) { userId =>
      pathEnd {
        parameter("userType") { userType =>
          val k: Future[User.State] = ServiceProxy.userCreate(proxy, userType, userId)
          complete("put success")
        }
      }
    }
  }

  def route: Route = postUser ~ getUser ~ putUser
}


/**
  * Created by hary on 16/12/2.
  */
object UserRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new UserRoute(proxy)

  def route(proxy: ActorRef): Route = UserRoute(proxy).route
}

