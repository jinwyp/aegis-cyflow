package com.yimei.cflow.http

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.core.Flow.DataPoint
import com.yimei.cflow.core.FlowProtocol
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.{User, UserProtocol}
import io.swagger.annotations._

/**
  * Created by hary on 16/12/6.
  */
@Path("/user/:userId/task/:taskId")
class TaskRoute(proxy: ActorRef) extends UserProtocol with FlowProtocol with SprayJsonSupport {

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
  def postTask = post {
    pathPrefix("user" / Segment / "task" / Segment) { (userId, taskId) =>
      entity(as[Map[String, DataPoint]]) { points =>
        println("hello world!!!")
        complete(ServiceProxy.userSubmit(proxy, userId,taskId, points))
      }
    }
  }

  def route: Route = postTask
}

/**
  * Created by hary on 16/12/2.
  */
object TaskRoute {
  def apply(proxy: ActorRef) = new TaskRoute(proxy)
  def route(proxy: ActorRef): Route = TaskRoute(proxy).route
}

