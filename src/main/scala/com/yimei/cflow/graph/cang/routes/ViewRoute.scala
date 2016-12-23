package com.yimei.cflow.graph.cang.routes

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.util.Timeout
import com.yimei.cflow.api.models.flow.FlowProtocol
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.api.models.user.State
import io.swagger.annotations._

import scala.concurrent.duration._

//     /flow/:userId

/**
  * Created by hary on 16/12/7.
  */
@Path("/cang")
class ViewRoute(proxy: ActorRef) extends FlowProtocol with SprayJsonSupport {

  implicit val timeout = ViewRoute.cangServiceTimeout // todo  why import User.userServiceTimeout does not work

  /**
    * 为用户创建流程
    */
  @ApiOperation(value = "flowState", notes = "", nickname = "创建流程", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "创建流程",
      required = true,
      dataType = "com.yimei.cflow.core.Cang.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def postCang: Route = post {
    pathPrefix("flow") {
      pathEnd {
        parameters(('userType, 'userId, 'flowType)) { (userType, userId, flowType) =>
            complete(ServiceProxy.flowCreate(proxy, userType, userId, flowType))
          // todo 1: add hierachy info support
          // todo 2: idempotent processing in backend
        }
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
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getCang: Route = get {
    pathPrefix("flow" / Segment) { flowId =>
      pathEnd {
        complete(ServiceProxy.flowGraph(proxy, flowId))
      }
    }
  }


  /**
    * 更新流程数据点并触发流程继续
    * PUT /flow/ying-hary-11111111111111?updatePoint
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
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def putCangPoints: Route = put {
    pathPrefix("flow" / Segment) { flowId =>
      pathEnd {
        parameters("points", "trigger") { (p, trigger) =>
          entity(as[Map[String, String]]) { points =>
            val ips = points.map { entry =>
              entry._1 -> entry._2
            }
            complete(ServiceProxy.flowUpdatePoints(proxy, flowId, ips, false))
          }
        }
      }
    }
  }

  def route: Route = postCang ~ getCang ~ putCangPoints

}


/**
  * Created by hary on 16/12/2.
  */
object ViewRoute {

  implicit val cangServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new ViewRoute(proxy)

  def route(proxy: ActorRef): Route = ViewRoute(proxy).route
}

