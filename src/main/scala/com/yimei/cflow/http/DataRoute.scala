package com.yimei.cflow.http

import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import io.swagger.annotations._

/**
  * Created by hary on 16/12/6.
  */



@Path("/data/:name?flowId=xxxx")
class DataRoute(proxy: ActorRef) {

  /**
    * 重新获取外部数据
    *
    * 后端的处理逻辑为:
    * 1. 用flowId查询flow 获取flow state
    * 2. 用flow state 向 autoActor 发起数据获取调用
    * 3. 给前端返回什么呢????
    *
    * @return
    */
  @ApiOperation(value = "dataFetch", notes = "", nickname = "获取外部数据", httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(
      name = "body",
      value = "触发自动任务获取数据",
      required = true,
      dataType = "com.yimei.cflow.user.Data.State",
      paramType = "body"
    )
    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
  ))
  @ApiResponses(Array(
    // new ApiResponse(code = 200, message = "服务器应答", response = classOf[Flow.State]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getData = pathPrefix("data" / Segment) { name =>
    pathEnd {
      parameter("flowId") { flowId =>
        complete(s"$name + $flowId")
      }
    }
  }

  def route: Route = getData
}


/**
  * Created by hary on 16/12/2.
  */
object DataRoute {
  def apply(proxy: ActorRef) = new DataRoute(proxy)
  def route(proxy: ActorRef): Route = DataRoute(proxy).route
}

