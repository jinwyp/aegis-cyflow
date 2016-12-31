package com.yimei.cflow.organ.routes

import java.sql.Timestamp
import java.time.Instant
import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.yimei.cflow.api.http.models.UserModel._
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._
import com.yimei.cflow.api.models.user.{State => UserState}
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.api.util.DBUtils
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.graph.cang.models.UserModel.UserLogin
import com.yimei.cflow.graph.cang.session.{MySession, SessionProtocol}
import com.yimei.cflow.organ.db.{PartyInstanceTable, PartyUserTable}
import DBUtils._
import com.yimei.cflow.graph.cang.exception.BusinessException
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}
import slick.lifted

import scala.concurrent.Future
import scala.concurrent.duration._


@Path("/user/:userId")
class UserRoute(proxy: ActorRef) extends UserModelProtocol with SprayJsonSupport with PartyUserTable with PartyInstanceTable with SessionProtocol{

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
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[UserState]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def postUser: Route = post {
    pathPrefix("user" / Segment / Segment  / Segment) { (party,instance_id,userId) =>
       {
         entity(as[UserInfo]) { user =>

          val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
              p.party_class === party &&
              p.instance_id === instance_id
           ).result.head) recover {
            case _ => throw new DatabaseException("不存在该公司")
          }

           def insert(p:PartyInstanceEntity): Future[PartyUserEntity] = {
             dbrun(partyUser returning partyUser.map(_.id) into ((pu,id)=>pu.copy(id=id)) +=
               PartyUserEntity(None,p.id.get,userId,user.password,user.phone,user.email,user.name,user.username, 0,Timestamp.from(Instant.now))) recover {
               case e =>
                  log.error("{}",e)
                 throw new DatabaseException("添加用户错误")
               //case a:SQLIntegrityConstraintViolationException => PartyUserEntity(None,p.id.get,userId,user.password,user.phone,user.email,user.name,Timestamp.from(Instant.now))
             }
           }
           val result: Future[UserState] = (for {
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
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[UserState]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def getUser: Route = get {
    pathPrefix("user" / Segment / Segment  / Segment) { (party,instance_id,userId) =>
      pathEnd {
        val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
          p.party_class === party &&
            p.instance_id === instance_id
        ).result.head) recover {
          case _ => throw new DatabaseException("不存在该公司")
        }

        def getUser(p:PartyInstanceEntity): Future[PartyUserEntity] = {
          dbrun(partyUser.filter(u=>
            u.user_id===userId &&
            u.party_id===p.id &&
            u.disable === 0
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该用户")
          }
        }

        val result: Future[QueryUserResult] = for{
          p <- pi
          u <- getUser(p)
          s <- ServiceProxy.userQuery(proxy, p.party_class+"-"+p.instance_id, u.user_id)
        } yield {
          QueryUserResult(u,s)
        }
        complete(result)
        }
      }

  }


  def getUserList: Route = get {
    pathPrefix("user" / Segment / Segment ) { (party,instance_id) =>
        ( parameter('limit.as[Int]) & parameter('offset.as[Int]) ) { (limit,offset) =>

          val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
            p.party_class === party &&
              p.instance_id === instance_id
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该公司")
          }

          def getUserList(p: PartyInstanceEntity): Future[Seq[PartyUserEntity]] = {
            dbrun(partyUser.filter(u =>
                u.party_id === p.id &&
                u.disable === 0
            ).drop(offset).take(limit).result) recover {
              case _ => throw new DatabaseException("不存在该用户")
            }
          }

          def getTotal(p: PartyInstanceEntity) = {
            dbrun(partyUser.filter(u =>
              u.party_id === p.id &&
              u.disable === 0
            ).length.result) recover {
              case _ => throw new DatabaseException("不存在该用户")
            }
          }

          val result: Future[UserListEntity] = for {
            p <- pi
            u <- getUserList(p)
            total <- getTotal(p)
          } yield {
            UserListEntity(u,total)
          }
          complete(result)
        }
      }
    }


  /**
    *更新用户
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
    new ApiResponse(code = 200, message = "服务器应答", response = classOf[UserState]),
    new ApiResponse(code = 500, message = "Internal server error")
  ))
  def putUser: Route = put {
    pathPrefix("user" / Segment / Segment / Segment) { (party,instance_id,userId)  =>
      entity(as[UserInfo]) { user =>

        val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
          p.party_class === party &&
            p.instance_id === instance_id
        ).result.head) recover {
          case _ => throw new DatabaseException("不存在该公司")
        }

        def updateUser(p:PartyInstanceEntity): Future[String] = {

          val pu = partyUser.filter(u=>
            u.user_id===userId &&
              u.party_id===p.id
          ).map(t=>(t.password,t.phone,t.email,t.name,t.username)).update(
            user.password,user.phone,user.email,user.name,user.username
          )

          dbrun(pu) map { i =>
            i match {
              case 1 => "success"
              case _ => "fail"
            }
          } recover {
            case _ => "fail"
          }
        }
        complete(for {
          p <- pi
          r <- updateUser(p)
        } yield {
          r
        })
      }
    }
  }

  def getLoginUserInfo: Route = post {
    (pathPrefix("login") & entity(as[UserLogin])) { user =>
      val getInfo: Future[Seq[(String, String, String, String, String)]] = dbrun((for {
        (pu, pi) <- partyUser join partyInstance on (_.party_id === _.id) if (pu.username === user.username && pu.password === user.password && pu.disable === 0)
      } yield (pu.username, pu.user_id, pi.party_class, pi.instance_id, pi.party_name)).result)


      def getResult(info: Seq[(String, String, String, String, String)]): MySession = {
        if(info.length == 0) {
          throw BusinessException("登录信息有误！")
        } else {
          MySession(info.head._1, info.head._2, info.head._3, info.head._4, info.head._5)
        }
      }

      val result = for {
        info <- getInfo
      } yield getResult(info)

      complete(result)
    }
  }

  def disAbleUser: Route = get {
    pathPrefix("disable" / Segment) { userId =>
      val pu = partyUser.filter(u =>
        u.user_id === userId &&
        u.disable === 0
      ).map(t => (t.disable)).update(1)

      complete(dbrun(pu) map { i =>
        i match {
          case 1 => "success"
          case _ => "fail"
        }
      } recover {
        case _ => "fail"
      })
    }
  }

  def route: Route = postUser ~ getUser ~ putUser ~ getUserList ~ getLoginUserInfo ~ disAbleUser
}


/**
  * Created by hary on 16/12/2.
  */
object UserRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new UserRoute(proxy)

  def route(proxy: ActorRef): Route = UserRoute(proxy).route

}

