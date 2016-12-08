package com.yimei.cflow.http

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import com.yimei.cflow.core.FlowProtocol
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.User
import io.swagger.annotations._

import scala.concurrent.duration._

//     /flow/:userId

/**
  * Created by hary on 16/12/7.
  */
@Path("/flow")
class FlowRoute(proxy: ActorRef) extends FlowProtocol with SprayJsonSupport {

  implicit val timeout = FlowRoute.flowServiceTimeout // todo  why import User.userServiceTimeout does not work

  /**
    * 为用户创建流程
    */
  @ApiOperation(value = "flowState", notes = "", nickname = "创建流程", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "创建流程",
      required = true,
      dataType = "com.yimei.cflow.core.Flow.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def postFlow: Route = post {
    pathPrefix("flow") {
      parameters(('userId, 'flowType)) { (userId, flowType) =>
        complete(ServiceProxy.flowCreate(proxy, userId, flowType))
        // todo 1: add hierachy info support
        // todo 2: idempotent processing in backend
      }
    }
  }

  /**
    * 查询流程
    *
    * @return
    */
  @ApiOperation(value = "flowState", notes = "", nickname = "查询用户状态", httpMethod = "GET")
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
  def getFlow: Route = get {
    pathPrefix("flow" / Segment) { flowId =>
      pathEnd {
        complete(ServiceProxy.flowQuery(proxy, flowId))
      }
    }
  }


  /**
    * 更新流程数据点并触发流程继续!!!!
    *
    * @return
    */
  @ApiOperation(value = "flowState", notes = "", nickname = "查询用户状态", httpMethod = "GET")
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
  def putFlowUpdatePoints: Route = put {
    pathPrefix("flow" / Segment) { flowId =>
      pathEnd {
        parameter("updatePoint") { p =>
          entity(as[Map[String, String]]) { points =>
            val ips = points.map { entry =>
              entry._1 -> entry._2.toInt    // 转成Int
            }
            complete(ServiceProxy.flowUpdatePoints(proxy, flowId, ips))
          }
        }
      }
    }
  }

  def route: Route = postFlow ~ getFlow ~ putFlowUpdatePoints

}


/**
  * Created by hary on 16/12/2.
  */
object FlowRoute {

  implicit val flowServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new FlowRoute(proxy)

  def route(proxy: ActorRef): Route = FlowRoute(proxy).route
}

