package com.yimei.cflow.http

import java.sql.{SQLIntegrityConstraintViolationException, Timestamp}
import java.time.Instant
import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.User.State
import com.yimei.cflow.user.db.{PartyInstanceEntity, PartyInstanceTable, PartyUserEntity, PartyUserTable}
import com.yimei.cflow.user.{User, UserProtocol}
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}
import spray.json.DefaultJsonProtocol
import com.yimei.cflow.util.DBUtils._

import scala.concurrent.Future
import scala.concurrent.duration._



case class AddUserModel(password:String, phone:Option[String],email:Option[String], name:String, gid:Option[String])

trait UserModelProtocol extends DefaultJsonProtocol {

  implicit val addUserModelFormat = jsonFormat5(AddUserModel)
}


@Path("/user/:userId")
class UserRoute(proxy: ActorRef) extends UserProtocol with SprayJsonSupport with PartyUserTable with UserModelProtocol with PartyInstanceTable{

  implicit val timeout = UserRoute.userServiceTimeout // todo  why import User.userServiceTimeout does not work
  import driver.api._

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
    pathPrefix("user" / Segment / Segment  / Segment) { (party,instance_id,userId) =>
       {
         entity(as[AddUserModel]) { user =>

          val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
              p.party_class === party &&
              p.instance_id === instance_id
           ).result.head) recover {
            case _ => throw new DatabaseException("不存在该公司")
          }

           def insert(p:PartyInstanceEntity): Future[PartyUserEntity] = {
             dbrun(partyUser returning partyUser.map(_.id) into ((pu,id)=>pu.copy(id=id)) +=
               PartyUserEntity(None,p.id.get,userId,user.password,user.phone,user.email,user.name,Timestamp.from(Instant.now))) recover {

               case a:SQLIntegrityConstraintViolationException => PartyUserEntity(None,p.id.get,userId,user.password,user.phone,user.email,user.name,Timestamp.from(Instant.now))
             }
           }
           val result: Future[State] = (for {
             p <- pi
             u <- insert(p)
             r <- ServiceProxy.userCreate(proxy, p.party_class+"-"+p.instance_id, u.user_id)
           } yield {
            r
           } )

           complete(result)
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

