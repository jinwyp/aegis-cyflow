package com.yimei.cflow.graph.cang.services

import java.text.SimpleDateFormat
import java.util.{Date, Random, UUID}

import akka.event.{Logging, LoggingAdapter}
import com.yimei.cflow.api.http.client.PartyClient
import com.yimei.cflow.api.http.client.UserClient
import com.yimei.cflow.api.util.HttpUtil._
import spray.json._
import DefaultJsonProtocol._
import com.yimei.cflow.api.http.models.PartyModel._
import com.yimei.cflow.api.http.models.ResultModel.{Error, Meta, Result}
import com.yimei.cflow.api.http.models.UserModel.{DynamicQueryUser, QueryUserResult, UserInfo, UserListEntity}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.PartyInstanceEntity
import com.yimei.cflow.api.models.user.State
import com.yimei.cflow.graph.cang.exception.BusinessException

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.models.UserModel.{AddCompany, AddUser, UpdateSelf, UpdateUser, UserChangePwd, UserData, UserLogin}
import com.yimei.cflow.graph.cang.session.{MySession, Session}


//import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by xl on 16/12/26.
  */
object LoginService extends PartyClient with UserClient with Config with PartyModelProtocal {

  //融资方进入仓压
  def financeSideEnter(userId: String, companyId: String, userInfo: AddUser): Future[String] = {
    log.info(s"get into financeSideEnter method: userId: ${userId}, companyId: ${companyId}, companyName: ${userInfo.companyName}")

    val exist: Future[Boolean] = for {
      qpi <- queryPartyInstance(rzf, companyId)
    } yield { qpi.toList.length > 0 }

    val p = Promise[String]()

    def add(exitst: Boolean): Future[String] = {
      if(!exitst){
        for {
          cpi <- createPartyInstance(PartyInstanceInfo(rzf, companyId, userInfo.companyName).toJson.toString)
          cu <- createPartyUser(rzf, cpi.instanceId, userId, userInfo.toJson.toString)
        } yield {
          "success"
        }
      }else{
        p.success("exist").future
      }
    }

    for {
      e <- exist
      result <- add(e)
    } yield result
  }

  //添加资金方
  def addInvestor(userId: String, instance_id: String, userInfo: AddUser): Future[Result[State]] = {
    log.info(s"get into addInvestor method: userId: ${userId}, instance_id: ${instance_id}, userInfo: ${userInfo}")

    //调用aegis-service接口,判断该资金方是否注册易煤网，并开通资金账户
    val queryYimei = Promise[Boolean].success(true).future //todo

    val gid: Int = if(userInfo.className == zjfyw) 1 else 2

    def add(qym: Boolean): Future[State] = {
      if(qym == true) {
        for {
          cp <- createPartyInstance(PartyInstanceInfo(zjf, instance_id, userInfo.companyName).toJson.toString)
          cu <- createPartyUser(zjf, instance_id, userId, userInfo.toJson.toString)
          cug <- createUserGroup(cp.id.get.toString, gid.toString, cu.userId)
        } yield cu
      }  else {
        throw new BusinessException("资金方没有注册易煤网，或者没有开通资金账户")
      }
    }

    val p = Promise[Result[State]]()

    val s = (for {
      qym <- queryYimei
      res <- add(qym)
    }
    yield {
      log.info("success insert data")
      p success Result(data = Some(res), success = true, error = null, meta = null)
    } ) recover {
      case e: BusinessException => {
        log.info(s"error happen, ${e.message}")
        p success  Result(data = None, success = false, error = Error(code = 111, message = e.message, field =""), meta = null)
      }
    }

    p.future
  }

  //管理员添加公司
  def adminAddCompany(companyInfo: AddCompany): Future[Result[PartyInstanceEntity]] = {
    log.info(s"get into method adminAddCompany, companyName:${companyInfo.companyName}, partyClass:${companyInfo.partyClass}")

    val formatter = new SimpleDateFormat("yyMMddhh")
    def instanceId = (formatter.format(new Date()).toInt  + new Random().nextInt(75)).toString

    val partyInstanceInfo = PartyInstanceInfo(party = companyInfo.partyClass, instanceId = instanceId, companyName = companyInfo.companyName)

    for {
      re <- createPartyInstance(partyInstanceInfo.toJson.toString)
    } yield Result(data = Some(re), success = true)
  }

  def adminGetSpecificCompany(partyClass: String, instanceId: String): Future[Result[PartyInstanceEntity]] = {
    log.info(s"get into method adminGetSpecificCompany, partyClass:${partyClass}, instanceId:${instanceId}")

    for {
      pi <- queryPartyInstance(partyClass, instanceId)
    } yield Result(data = Some(pi.head), success = true)
  }

  //管理员获取用户列表
  def adminGetAllUser(page: Int, pageSize: Int, dynamicQuery: DynamicQueryUser): Future[Result[List[UserData]]] = {
    log.info(s"get into method adminGetAllUser")
    for {
      userList <- getAllUserList(page, pageSize, dynamicQuery.toJson.toString)
    } yield Result(data = Some(userList.datas), success = true, meta = Meta(total = userList.total, count = pageSize, offset = (page - 1) * pageSize, page))
  }

  //管理员获取所有公司信息
  def adminGetAllCompany(page: Int, pageSize: Int, companyName: Option[String]): Future[Result[List[PartyInstanceEntity]]] = {
    log.info(s"get into method adminGetAllCompany: page:${page}, pageSize:${pageSize}, companyName:${companyName}")

    def getResult(list: Seq[PartyInstanceEntity]) = {
      import scala.collection.mutable.MutableList
      var result = MutableList[PartyInstanceEntity]()
      list.toList.foreach { info =>
        val temp = PartyInstanceEntity(id = info.id, partyClass = info.partyClass, instanceId = info.partyClass + "/" + info.instanceId, companyName = info.companyName, disable = info.disable, ts_c = info.ts_c)
        result += temp
      }
      result.toList
    }

    for {
      pilist <- getAllPartyInstanceList(page, pageSize, companyName)
      result = getResult(pilist.partyInstanceList)
    } yield Result(data = Some(result), success = true, meta = Meta(total = pilist.total, count = pageSize, offset = (page - 1) * pageSize, page))//total:Int, count:Int, offset:Int, page:Int)
  }

  //管理员修改公司信息
  def adminUpdateCompany(party: String, instanceId: String, companyName: String): Future[Result[String]] = {
    log.info(s"get into method adminUpdateCompany, party:${party}, intanceId:${instanceId}, companyName:${companyName}")
    for {
      re <- updatePartyInstance(party, instanceId, companyName)
    } yield Result(data = Some(re), success = true)
  }

  //管理员修改用户
  def adminModifyUser(party: String, instance_id: String, userInfo: UpdateUser): Future[Result[UserData]] = {
    log.info(s"get into method adminModifyUser, party=${party}, instance_id=${instance_id}, userInfo=${userInfo.toString}")
    val result = updatePartyUser(party, instance_id, userInfo.userid, userInfo.toJson.toString)

    def getResult(result: String): Result[UserData] = {
      if(result == "success"){
        Result(data = Some(UserData(userId = userInfo.userid, username = userInfo.username, email = userInfo.email, mobilePhone = userInfo.phone, role = party, companyId = "", companyName = "")), success = true, error = null, meta = null)
      }else {
        Result(data = None, success = false, meta = null)
      }
    }

    for {
      re <- result
    } yield getResult(re)
  }

  //用户修改自己信息
  def userModifySelf(party: String, instance_id: String, userId: String, userInfo: UpdateSelf): Future[Result[UserData]] = {
    log.info(s"get into method userModifySelf, party=${party}, instance_id=${instance_id}, userInfo=${userInfo.toString}")

    val getPartyUser: Future[QueryUserResult] = getSpecificPartyUser(party, instance_id, userId)
    def update(qur: QueryUserResult): Future[String] = {
      updatePartyUser(party, instance_id, userId, UserInfo(qur.userInfo.password, Some(userInfo.phone), Some(userInfo.email), qur.userInfo.name, qur.userInfo.username).toJson.toString)
    }

    def getResult(result: String, qur: QueryUserResult): Result[UserData] = {
      if(result == "success"){
        Result(data = Some(UserData(qur.userInfo.user_id, qur.userInfo.username, userInfo.email, userInfo.phone, party, "", "")), success = true, error = null, meta = null)
      }else {
        Result(data = None, success = false, meta = null)
      }
    }

    for {
      qur <- getPartyUser
      re <- update(qur)
    } yield getResult(re, qur)
  }

  //用户登录
  def getLoginUserInfo(userLogin: UserLogin): Future[MySession] = {
    log.info(s"get into method getLoginUserInfo, username:${userLogin.username}")
    getLoginUserInfo(userLogin.toJson.toString)
  }

  //用户修改密码
  def userModifyPassword(party: String, instance_id: String, userId: String, user: UserChangePwd): Future[Result[UserData]] = {
    log.info(s"get into method userModifyPassword, userId:${userId}, party:${party}, instance_id:${instance_id}")

    val getPartyUser: Future[QueryUserResult] = getSpecificPartyUser(party, instance_id, userId)

    def update(qur: QueryUserResult): Future[String] = {
      updatePartyUser(party, instance_id, userId, UserInfo(user.newPassword, qur.userInfo.phone, qur.userInfo.email, qur.userInfo.name, qur.userInfo.username).toJson.toString)
    }

    def comparePassword(qur: QueryUserResult, userInfo: UserChangePwd) = {
      if(qur.userInfo.password != userInfo.oldPassword) {
        throw BusinessException("您输入的旧密码有误！")
      }
      if(userInfo.oldPassword == userInfo.newPassword) {
        throw BusinessException("您输入的新旧密码相同！")
      }
    }

    def getResult(qur: QueryUserResult): UserData = {
      UserData(qur.userInfo.user_id, qur.userInfo.username, qur.userInfo.email.getOrElse(""), qur.userInfo.phone.getOrElse(""), party, "", "")
    }

    (for {
      qur <- getPartyUser
      cr = comparePassword(qur, user)
      ur <- update(qur)
      re = getResult(qur)
    } yield { Result(data = Some(re), success = true)}) recover {
      case e: BusinessException => Result[UserData](data = None, success = false, error = Error(code = 111, message = e.message, field = ""))
    }
  }

  //管理员重置用户密码
  def adminResetUserPassword(party: String, instance_id: String, userId: String): Future[Result[String]] = {
    log.info(s"get into method adminResetUserPassword, userId:${userId}, party:${party}, instance_id:${instance_id}")

    val getPartyUser: Future[QueryUserResult] = getSpecificPartyUser(party, instance_id, userId)

    val newPassword = "111111"
    def update(qur: QueryUserResult): Future[String] = {
      updatePartyUser(party, instance_id, userId, UserInfo(newPassword, qur.userInfo.phone, qur.userInfo.email, qur.userInfo.name, qur.userInfo.username).toJson.toString)
    }

    for {
      qur <- getPartyUser
      ur <- update(qur)
    } yield { Result(data = Some(ur), success = true)}
  }

  //管理员查询用户列表
  def adminGetUserList(party: String, instance_id: String, limit: Int, offset: Int): Future[Result[UserListEntity]] = {
    log.info(s"get into method adminGetUserList, party:${party}, instance_id:${instance_id}")
    for {
      list <- getUserList(party, instance_id, limit, offset)
    } yield Result(data = Some(list), success = true)
  }

  //管理员禁用用户
  def adminDisableUser(userId: String): Future[Result[String]] = {
    log.info(s"get into method adminDisableUser, userId:${userId}")

    def getResult(result: String): Result[String] = {
      if(result == "success"){
        Result(data = Some(result), success = true, error = null, meta = null)
      }else {
        Result(data = None, success = false, meta = null)
      }
    }

    for {
      re <- disableUser(userId)
    } yield getResult(re)
  }
}
