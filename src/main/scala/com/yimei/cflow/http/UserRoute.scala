package com.yimei.cflow.http

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.user.{User, UserProtocol}
import com.yimei.cflow.user.User.{CommandQueryUser, State}
import io.swagger.annotations._
import concurrent.duration._
import scala.concurrent.Future

@Path("/user/:userId")
class UserRoute(proxy: ActorRef) extends UserProtocol {

  implicit val timeout = UserRoute.userServiceTimeout   // todo  why import User.userServiceTimeout does not work

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
    val fstate: Future[State] = (proxy ? CommandQueryUser(userId)).mapTo[User.State]
    complete(fstate)
  }

  def route: Route = getUser

}


/**
  * Created by hary on 16/12/2.
  */
object UserRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new UserRoute(proxy)

  def route(proxy: ActorRef): Route = UserRoute(proxy).route
}

