package com.yimei.cflow.http

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import io.swagger.annotations._

/**
  * Created by hary on 16/12/6.
  */


@Path("/user/:userId")
class DataRoute(proxy: ActorRef) {

  @ApiOperation(value = "userState", notes = "", nickname = "查询用户状态", httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "查询用户状态",
      required = true,
      dataType = "com.yimei.cflow.user.Data.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    // new ApiResponse(code = 200, message = "服务器应答", response = classOf[Data.State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getData = pathPrefix("/user" / Segment) { userId => complete("hello") }

  def route: Route = ???
}





/**
  * Created by hary on 16/12/2.
  */
object DataRoute {
  def apply(proxy: ActorRef) = new DataRoute(proxy)
  def route(proxy: ActorRef): Route = DataRoute(proxy).route
}

