package com.yimei.cflow.organ.routes

import java.sql.Timestamp
import java.time.Instant
import java.util.UUID
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
import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.graph.cang.session.{MySession, SessionProtocol}
import com.yimei.cflow.organ.db.{PartyInstanceTable, PartyUserTable, UserGroupTable}
import DBUtils._
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.UserModel.{UserData, UserInfoList}
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}
import slick.backend.DatabasePublisher
import slick.dbio.Effect.Read
import slick.lifted
import slick.profile.{FixedSqlStreamingAction, SqlStreamingAction}

import scala.collection.mutable
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

@Path("/user/:userId")
class UserRoute(proxy: ActorRef) extends UserModelProtocol with SprayJsonSupport with PartyUserTable with UserGroupTable with PartyInstanceTable with SessionProtocol{

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
             r <- ServiceProxy.userCreate(proxy, p.partyClass+"-"+p.instanceId, u.user_id)
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
          s <- ServiceProxy.userQuery(proxy, p.partyClass+"-"+p.instanceId, u.user_id)
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
    (pathPrefix("login") & entity(as[UserLoginInfo])) { user =>
      def getUserInfo(username: String, password: String): Future[Vector[(String, String, String, String, String, String, String, String)]] = {
        val query = sql"""
             select pu.username, pu.user_id, pu.email, pu.phone, pi.party_class, ug.gid, pi.instance_id, pi.party_name
             from party_instance pi join party_user pu on pu.party_id = pi.id
             left join user_group ug on pu.user_id = ug.user_id
             where pu.username = $username  and pu.password = $password and pu.disable = 0
             """
        dbrun(query.as[(String, String, String, String, String, String, String, String)])
      }

      def getResult(info: Seq[(String, String, String, String, String, String, String, String)]): UserGroupInfo = {
       println("-------------" + info.length)
        if(info.length == 0) {
          throw BusinessException("登录信息有误！")
        } else {
          val gid = if(info.head._6 == null) None else Some(info.head._6)
          UserGroupInfo(info.head._1, info.head._2, info.head._3, info.head._4, info.head._5, gid, info.head._7, info.head._8)
        }
      }

      val result = for {
        info <- getUserInfo(user.username, user.password)
        re = getResult(info)
      } yield re

      complete(result)
    }
  }

  def getAllUserInfo: Route = post {
    (path("alluser") & parameter('page.as[Int]) & parameter('pageSize.as[Int])) { (page, pageSize) =>
      entity(as[DynamicQueryUser]) { qi =>

        if(page <= 0 || pageSize <= 0) throw BusinessException("分页参数有误！")

        def getUserInfo(userName: String, companyName: String, l: Int, os: Int): Future[Vector[(String, String, String, String, String, String, String, String)]] = {
          val query = sql"""
             select pu.user_id, pu.username, pu.email, pu.phone, pi.party_class, ug.gid, pi.instance_id, pi.party_name
             from party_instance pi join party_user pu on pu.party_id = pi.id
             left join user_group ug on pu.user_id = ug.user_id
             where pu.disable = 0 and pi.party_name like $companyName and pu.username like $userName limit $l offset $os
             """
          dbrun(query.as[(String, String, String, String, String, String, String, String)])
        }

        def getAccount(userName: String, companyName: String): Future[Int] = {
         val query = sql"""
             select count(1)
             from party_instance pi join party_user pu on pu.party_id = pi.id
             left join user_group ug on pu.user_id = ug.user_id
             where pu.disable = 0 and pi.party_name like $companyName and pu.username like $userName
             """
          for {
            account <- dbrun(query.as[Int])
          } yield account.toList.head

        }

        def getResult(Info: Seq[(String, String, String, String, String, String, String, String)]): List[UserData] = {
          import scala.collection.mutable.MutableList
          var result = MutableList[UserData]()
          Info.toList.foreach{ info =>
            val role = if(info._6 == null || info._6 == "1") info._5 else info._5 + "Accountant"
            result += UserData(info._1, info._2, info._3, info._4, role, info._7, info._8)
          }
          result.toList
        }

        val userName = "%" + qi.userName.getOrElse("") + "%"
        val companyName = "%" + qi.companyName.getOrElse("") + "%"
        val result = for {
          info <- getUserInfo(userName, companyName, pageSize, (page - 1) * pageSize)
          list = getResult(info)
          total <- getAccount(userName, companyName)
        } yield UserInfoList(datas = list, total = total)

        complete(result)
      }
    }
  }

  def disAbleUser: Route = get {
    pathPrefix("disable" / Segment) { username =>
      val pu = partyUser.filter(u =>
        u.username === username &&
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

  def getUserInfoByUserName: Route = {
    path("specificUser" / Segment) { username =>
      def getUserInfo(username: String): Future[Vector[(String, String, String, String, String, String, String, String)]] = {
        val query = sql"""
             select pu.username, pu.user_id, pu.email, pu.phone, pi.party_class, ug.gid, pi.instance_id, pi.party_name
             from party_instance pi join party_user pu on pu.party_id = pi.id
             left join user_group ug on pu.user_id = ug.user_id
             where pu.username = $username
             """
        dbrun(query.as[(String, String, String, String, String, String, String, String)])
      }

      def getResult(info: Seq[(String, String, String, String, String, String, String, String)]): UserGroupInfo = {
        if(info.length == 0) {
          throw BusinessException("不存在该用户名对应的用户信息！")
        } else {
          val gid = if(info.head._6 == null) None else Some(info.head._6)
          UserGroupInfo(info.head._1, info.head._2, info.head._4, info.head._3, info.head._5, gid, info.head._7, info.head._8)
        }
      }

      val result = for {
        info <- getUserInfo(username)
        re = getResult(info)
      } yield re

      complete(result)
    }
  }

  def modifyEmailAndPhone: Route = put {
    path("user" / Segment / Segment / Segment / "emailAndPhone") {(username, email, phone) =>
      val pu = partyUser.filter(u=>
        u.username === username &&
        u.disable === 0
      ).map(t => (t.phone, t.email)).update(Some(phone), Some(email))


      complete(dbrun(pu) map { i =>
        i match {
          case 1 => "success"
          case _ => throw BusinessException("不存在该用户")
        }
      } recover {
        case _ => throw BusinessException("不存在该用户")
      })
    }
  }

  def route: Route = postUser ~ getUser ~ putUser ~ getUserList ~ getLoginUserInfo ~ disAbleUser ~ getAllUserInfo ~ getUserInfoByUserName ~
    modifyEmailAndPhone
}


/**
  * Created by hary on 16/12/2.
  */
object UserRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new UserRoute(proxy)

  def route(proxy: ActorRef): Route = UserRoute(proxy).route

}

