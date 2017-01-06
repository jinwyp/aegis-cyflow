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
import slick.profile.FixedSqlStreamingAction

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
      val getInfo: Future[Seq[(String, String, String, String, String, String, String)]] = dbrun((for {
        (pu, pi) <- partyUser join partyInstance on (_.party_id === _.id) if (pu.username === user.username && pu.password === user.password && pu.disable === 0)
      } yield (pu.username, pu.user_id, pu.email.getOrElse(""), pu.phone.getOrElse(""), pi.party_class, pi.instance_id, pi.party_name)).result)

      def uuid() = UUID.randomUUID().toString

      def getResult(info: Seq[(String, String, String, String, String, String, String)]): MySession = {
        if(info.length == 0) {
          throw BusinessException("登录信息有误！")
        } else {
          MySession(uuid, info.head._1, info.head._2, info.head._3, info.head._4, info.head._5, info.head._6, info.head._7)
        }
      }

      val result = for {
        info <- getInfo
      } yield getResult(info)

      complete(result)
    }
  }

  def getAllUserInfo: Route = get {
    path("alluser") {
      (parameter('page.as[Int]) & parameter('pageSize.as[Int])) { (page, pageSize) =>
        if(page <= 0 || pageSize <= 0) throw BusinessException("分页参数有误！")

        val userInfoQuery = for {
          (pu, pi) <- partyUser join partyInstance on (_.party_id === _.id) if(pu.disable === 0)
        } yield (pu.user_id, pu.username, pu.email.getOrElse(""), pu.phone.getOrElse(""), pi.instance_id, pi.party_name, pi.party_class)

        val getUserInfo = dbrun(userInfoQuery.drop((page - 1) * pageSize).take(pageSize).result)

        val getTotal = dbrun(userInfoQuery.length.result)

        def getGroupInfo(userId: String, party: String): Future[String] = {
          dbrun((for {
            (pu, pi) <- partyUser join userGroup on (_.user_id === _.user_id) if(pu.user_id === userId)
          } yield (pu.user_id, pi.gid)).result) map { info =>
            if(info.isEmpty) party else {
              if(info.head._2 == "2") party + "Accountant" else party
            }
          }
        }

        def getResult(userInfo: Seq[(String, String, String, String, String, String, String)]): Future[List[UserData]] = {
          import scala.collection.mutable.MutableList
          var result = MutableList[Future[UserData]]()
          userInfo.toList.foreach{ info =>

            val temp = for {
              role <- getGroupInfo(info._1, info._7)
            } yield {
              UserData(userId = info._2, username = info._1, email = info._3, mobilePhone = info._4, role = role, companyId = info._5, companyName = info._6)
            }

            result += temp
          }
          Future.sequence(result.toList)
        }

        val result = for {
          info <- getUserInfo
          list <- getResult(info)
          total <- getTotal
        }yield UserInfoList(datas = list, total = total)

        complete(result)
      }
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

  def dynamicSearchOfUserList(): Route = post {
    (pathPrefix("dynamicSearch") & entity(as[DynamicUserSearch])) { uls =>

      //      val piq: FixedSqlStreamingAction[Seq[PartyInstanceEntity], PartyInstanceEntity, Read] = partyInstance.filter { p =>
      //        List(
      //          uls.partyClass.map(p.party_class === _),
      //          uls.companyName.map(p.party_class === _)
      //        ).collect({ case Some(criteria) => criteria }).reduceLeftOption(_ || _).getOrElse(true: Rep[Boolean])
      //      }.result
      //
      //      val uiq: Future[Unit] = db.stream(piq).foreach(pi => {
      //        dbrun(partyUser.filter( p =>
      //          List(
      //            uls.username.map(p.username === _),
      //            uls.name.map(p.name === _)
      //          ).collect({case Some(criteria) => criteria}).reduceLeftOption(_ || _).getOrElse(true: Rep[Boolean])
      //        ).result)
      //      }
      //      )
      //username: Option[String], name: Option[String], companyName: Option[String], partyClass: Option[String])

      val username = if (uls.username.isDefined) uls.username.get



//      val getInfo: Future[Seq[(String, String, String, String, String)]] = dbrun((for {
//        (pu, pi) <- partyUser join partyInstance on (_.party_id === _.id) //if (pu.username === user.username && pu.password === user.password && pu.disable === 0)
//        List(
//          uls.username.map(pu.username === _),
//          uls.name.map(pu.name === _),
//          uls.partyClass.map(pi.party_class === _),
//          uls.companyName.map(pi.party_name === _)
//        ).collect({ case Some(criteria) => criteria }).reduceLeftOption(_ || _).getOrElse(true: Rep[Boolean]).result
//      } yield (pu.username, pu.user_id, pi.party_class, pi.instance_id, pi.party_name)).result)


      //      def getResult(info: Seq[(String, String, String, String, String)]): MySession = {
      //        if(info.length == 0) {
      //          throw BusinessException("登录信息有误！")
      //        } else {
      //          MySession(info.head._1, info.head._2, info.head._3, info.head._4, info.head._5)
      //        }
      //      }
      //
      //      val result = for {
      //        info <- getInfo
      //      } yield getResult(info)
      //
      //      complete(result)


      complete("")
    }
  }

  def route: Route = postUser ~ getUser ~ putUser ~ getUserList ~ getLoginUserInfo ~ disAbleUser ~ getAllUserInfo
}


/**
  * Created by hary on 16/12/2.
  */
object UserRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new UserRoute(proxy)

  def route(proxy: ActorRef): Route = UserRoute(proxy).route

}

