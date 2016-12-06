package com.yimei.cflow.user

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Route
import io.swagger.annotations._
import akka.http.scaladsl.server.Directives._

//
//
// 这里需要访问userMaster!!!!!!!!!!!!!!!!!!!
// 如何获取userMaster???
//
//

@Path("/user/:userId")
class UserRoute(proxy: ActorRef) {

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
  def getUser = pathPrefix("/user" / Segment) { userId =>
    complete(s"user info for $userId")
  }

}

@Path("/user/:userId/task/:taskId")
class TaskRoute(proxy: ActorRef) {

  @ApiOperation(value = "userTask", notes = "", nickname = "用户提交任务", httpMethod = "POST")
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
  def postTask = pathPrefix("/user" / Segment / "task" / Segment) { (userId, taskId) =>
    complete(s"user info for $userId")
  }

}



/**
  * Created by hary on 16/12/2.
  */
object UserRoute {
  def route(proxy: ActorRef): Route = new TaskRoute(proxy).postTask
}
