package com.yimei.cflow.http

import java.sql.Timestamp
import java.time.Instant
import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.yimei.cflow.config.DatabaseConfig.{coreExecutor => _, _}
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.{User, UserProtocol}
import com.yimei.cflow.util.DBUtils._
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}

import scala.concurrent.Future
import scala.concurrent.duration._
import com.yimei.cflow.user.db._



//@Path("/group/:userId")
class GroupRoute(proxy: ActorRef) extends UserProtocol with PartyGroupTable with SprayJsonSupport {

  import driver.api._

//  implicit val timeout = GroupRoute.userServiceTimeout // todo  why import User.userServiceTimeout does not work
//
//  /**
//    * 创建用户
//    */
//  @ApiOperation(value = "userState", notes = "", nickname = "创建用户", httpMethod = "POST")
//  @ApiImplicitParams(Array(
//    new ApiImplicitParam(
//      name = "body",
//      value = "创建用户",
//      required = true,
//      dataType = "com.yimei.cflow.user.User.State",
//      paramType = "body"
//    )
//    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
//  ))
//  @ApiResponses(Array(
//    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
//    new ApiResponse(code = 500, message = "Internal server error")
//  ))
//  def postUser: Route = post {
//    pathPrefix("user" / Segment) { userId =>
//      pathEnd {
//        parameter('userType) { userType =>
//          complete(ServiceProxy.userCreate(proxy, userType, userId))
//        }
//      }
//    }
//  }

  def getGroupParty: Route = get {
    (pathPrefix("group") & parameter("limit".as[Int]) & parameter("offset".as[Int])) { (limit,offset) =>
      complete(dbrun(partyGroup.drop(offset).take(limit).result))
    }
  }

  def createGroupParty: Route = post {
    pathPrefix("group" / Segment) { pc =>
      pathEnd {
        parameter("gid") { gid =>
          val entity: Future[PartyGroupEntity] = dbrun(
            (partyGroup returning partyGroup.map(_.id)) into ((pg, id) => pg.copy(id = id)) += PartyGroupEntity(None, pc, gid, "", Timestamp.from(Instant.now))
          )
          complete(entity map { e => e})
        }
      }
    }
  }

  def deleteGroupParty: Route = delete {
    pathPrefix("group" / Segment) { pc =>
      pathEnd {
        parameter("gid") { gid =>
          val delete = partyGroup.filter(pg => pg.party_class === pc && pg.gid === gid).delete
          val result = dbrun(delete) map { count =>
            if(count > 0) "success" else "fail"
          }
          complete(result)
        }
      }
    }
  }

  def updateGroupParty: Route = put {
    pathPrefix("group") {
      (parameter("id".as[Long]) & parameter("party") & parameter("gid")) { (id, pc, gid) =>
        val update = partyGroup.filter(_.id === id).map(p => (p.party_class, p.gid)).update(pc, gid)
        val result = dbrun(update) map { count =>
          if(count > 0) "success" else "fail"
        }
        complete(result)
      }
    }
  }

//  // todo 1: add hierachy info support
//  // todo 2: idempotent processing in backend
//
//  /**
//    * 查询用户
//    *
//    * @return
//    */
//  @ApiOperation(value = "userState", notes = "", nickname = "查询用户状态", httpMethod = "GET")
//  @ApiImplicitParams(Array(
//    new ApiImplicitParam(
//      name = "body",
//      value = "查询用户状态",
//      required = true,
//      dataType = "com.yimei.cflow.user.User.State",
//      paramType = "body"
//    )
//    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
//  ))
//  @ApiResponses(Array(
//    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
//    new ApiResponse(code = 500, message = "Internal server error")
//  ))
//  def getUser: Route = get {
//    pathPrefix("user" / Segment) { userId =>
//      pathEnd {
//        parameter("userType") { userType =>
//          complete(ServiceProxy.userQuery(proxy, userType, userId))
//        }
//      }
//    }
//  }
//
//  /**
//    * 查询用户
//    *
//    * @return
//    */
//  @ApiOperation(value = "userState", notes = "", nickname = "查询用户状态", httpMethod = "PUT")
//  @ApiImplicitParams(Array(
//    new ApiImplicitParam(
//      name = "body",
//      value = "查询用户状态",
//      required = true,
//      dataType = "com.yimei.cflow.user.User.State",
//      paramType = "body"
//    )
//    // new ApiImplicitParam(name = "orgId",     value = "组织Id", required = false, dataType = "string", paramType = "path"),
//  ))
//  @ApiResponses(Array(
//    new ApiResponse(code = 200, message = "服务器应答", response = classOf[User.State]),
//    new ApiResponse(code = 500, message = "Internal server error")
//  ))
//  def putUser: Route = put {
//    pathPrefix("user" / Segment) { userId =>
//      pathEnd {
//        parameter("userType") { userType =>
//          val k: Future[User.State] = ServiceProxy.userCreate(proxy, userType, userId)
//          complete("put success")
//        }
//      }
//    }
//  }

//  def route: Route = postUser ~ getUser ~ putUser
  def route: Route = getGroupParty ~ createGroupParty ~ deleteGroupParty ~ updateGroupParty
}


/**
  * Created by hary on 16/12/2.
  */
object GroupRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new GroupRoute(proxy)

  def route(proxy: ActorRef): Route = GroupRoute(proxy).route
}

