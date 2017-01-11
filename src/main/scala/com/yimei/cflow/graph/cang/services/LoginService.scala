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
import com.yimei.cflow.api.http.models.ResultModel.{Error, PagerInfo, Result}
import com.yimei.cflow.api.http.models.UserModel.{UserGroupInfo, _}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.PartyInstanceEntity
import com.yimei.cflow.api.models.user.State
import com.yimei.cflow.graph.cang.exception.BusinessException

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.models.UserModel.{AddCompany, AddUser, UpdateSelf, UpdateUser, UserChangePwd, UserData, UserLogin}
import com.yimei.cflow.graph.cang.session.{MySession, Session}
import com.yimei.cflow.config.CoreConfig._


//import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by xl on 16/12/26.
  */
object LoginService extends PartyClient with UserClient with Config with PartyModelProtocol {

  //融资方进入仓压
  def financeSideEnter(userId: String, companyId: String, userInfo: AddUser): String = {
//    log.info(s"get into financeSideEnter method: userId: ${userId}, companyId: ${companyId}, companyName: ${userInfo.companyName}")
//
//    val exist: Future[Boolean] = for {
//      qpi <- queryPartyInstance(rzf, companyId)
//      cu <- createPartyUser(zjf, qpi.ins, userId, userInfo.toJson.toString)
//    } yield { qpi.toList.length > 0 }
//
//    val p = Promise[String]()
//
//    def add(exitst: Boolean): Future[String] = {
//      if(!exitst){
//        for {
//          cpi <- createPartyInstance(PartyInstanceInfo(rzf, companyId, userInfo.companyName).toJson.toString)
//          cu <- createPartyUser(rzf, cpi.instanceId, userId, userInfo.toJson.toString)
//        } yield {
//          "success"
//        }
//      }else{
//        p.success("exist").future
//      }
//    }
//
//    for {
//      e <- exist
//      result <- add(e)
//    } yield result
    "hehe"
  }

  //添加用户
  def addUser(userInfo: AddUser): Future[Result[State]] = {
    log.info(s"get into addInvestor method: userInfo: ${userInfo}")

    def getExistCompany(partyClass: String, instanceId: String): Future[PartyInstanceEntity] = {
      for {
        qpi <- queryPartyInstance(partyClass, instanceId)
      } yield {
        if(qpi.length > 0) qpi.head else throw BusinessException("没有对应的公司")
      }
    }

    val formatter = new SimpleDateFormat("yyMMddhh")
    def userId = (formatter.format(new Date()).toInt  + new Random().nextInt(75)).toString

    val password = "111111"
    def deal(info: AddUser) = {
      UserAddModel(info.username, password, info.name, info.email, info.phone, info.companyId, info.className)
    }

    val info = deal(userInfo)
    //邮件通知用户密码  //todo

    def isYimeiUser(): Future[Boolean] = {
      //调用aegis-service接口,判断该资金方是否注册易煤网，并开通资金账户
      val queryYimei = Promise[Boolean].success(true).future //todo
      for {
        qym <- queryYimei
      } yield {
        if(qym == true) true else throw BusinessException("该公司没有注册易煤网，或者没有开通资金账户！")
      }
    }


    val result: Future[State] = userInfo.className match {
      case e: String if(e == gkf) => {
        for {
          pie <- getExistCompany(gkf, userInfo.companyId)
          cu <- createPartyUser(zjf, pie.instanceId, userId, info.toJson.toString)
        } yield cu
      }
      case e: String if(e == jgf) => {
        for {
          pie <- getExistCompany(jgf, userInfo.companyId)
          cu <- createPartyUser(jgf, pie.instanceId, userId, info.toJson.toString)
        } yield cu
      }
      case e: String if(e == zjfyw) => {
        for {
          iyu <- isYimeiUser()
          pie <- getExistCompany(zjf, userInfo.companyId)
          cu <- createPartyUser(zjf, pie.instanceId, userId, info.toJson.toString) if iyu == true
          cug <- createUserGroup(pie.id.get.toString, 1.toString, cu.userId) if iyu == true
        } yield cu
      }
      case e: String if(e == zjfcw) => {
        for {
          iyu <- isYimeiUser
          pie <- getExistCompany(zjf, userInfo.companyId)
          cu <- createPartyUser(zjf, pie.instanceId, userId, info.toJson.toString) if iyu == true
          cug <- createUserGroup(pie.id.get.toString, 2.toString, cu.userId) if iyu == true
        } yield cu
      }
      case e: String if(e == rzf) => {
        for {
          iyu <- isYimeiUser
          pie <- getExistCompany(rzf, userInfo.companyId)
          cu <- createPartyUser(rzf, pie.instanceId, userId, info.toJson.toString) if iyu == true
        } yield cu
      }
      case _ => {
        throw BusinessException("贸易商、管理员暂时不支持添加用户")
      }
    }

    for {
      re <- result
    } yield Result[State](data = Some(re), success = true)
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

    def deal(entity: PartyInstanceEntity) = {
      PartyInstanceEntity(entity.id, entity.partyClass, entity.partyClass + "/" + entity.instanceId, entity.companyName, entity.disable, entity.ts_c)
    }

    val result: Future[List[PartyInstanceEntity]] = queryPartyInstance(partyClass, instanceId) map { (sq: List[PartyInstanceEntity]) =>
      sq.map(deal(_))
    }

    for {
      pi <- result
    } yield Result(data = Some(pi.head), success = true)
  }

  //管理员获取用户列表
  def adminGetAllUser(page: Int, pageSize: Int, dynamicQuery: DynamicQueryUser): Future[Result[List[UserData]]] = {
    log.info(s"get into method adminGetAllUser")
    for {
      userList <- getAllUserList(page, pageSize, dynamicQuery.toJson.toString)
    } yield Result(data = Some(userList.datas), success = true, meta = Some(PagerInfo(total = userList.total, count = pageSize, offset = (page - 1) * pageSize, page)))
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
    } yield Result(data = Some(result), success = true, meta = Some(PagerInfo(total = pilist.total, count = pageSize, offset = (page - 1) * pageSize, page)))//total:Int, count:Int, offset:Int, page:Int)
  }

  //管理员修改公司信息
  def adminUpdateCompany(party: String, instanceId: String, companyName: String): Future[Result[String]] = {
    log.info(s"get into method adminUpdateCompany, party:${party}, intanceId:${instanceId}, companyName:${companyName}")
    for {
      re <- updatePartyInstance(party, instanceId, companyName)
    } yield Result(data = Some(re), success = true)
  }

  //管理员修改用户
  def adminModifyUser(username: String, userInfo: UpdateUser): Future[Result[String]] = {
    log.info(s"get into method adminModifyUser, username=${username}, userInfo=${userInfo.toString}")
    for {
      re <- updatePartyUserEmailAndPhone(username, userInfo.email, userInfo.phone)
    } yield {
      if(re == "success") Result(data = Some("")) else Result(data = Some(""), success = false)
    }
  }

  //用户修改自己信息
  def userModifySelf(party: String, instance_id: String, userId: String, userInfo: UpdateSelf): Future[Result[UserData]] = {
    log.info(s"get into method userModifySelf, party=${party}, instance_id=${instance_id}, userInfo=${userInfo.toString}")

    def getEmail(qur: QueryUserResult): String = {
      if(userInfo.email.isDefined) userInfo.email.get else qur.userInfo.email.getOrElse("")
    }
    def getPhone(qur: QueryUserResult): String = {
      if(userInfo.phone.isDefined) userInfo.phone.get else qur.userInfo.phone.getOrElse("")
    }

    val getPartyUser: Future[QueryUserResult] = getSpecificPartyUser(party, instance_id, userId)
    def update(qur: QueryUserResult, email: String, phone: String): Future[String] = {
      updatePartyUser(party, instance_id, userId, UserInfo(qur.userInfo.password, Some(phone), Some(email), qur.userInfo.name, qur.userInfo.username).toJson.toString)
    }

    def getResult(result: String, qur: QueryUserResult, email: String, phone: String): Result[UserData] = {
      if(result == "success"){
        Result(data = Some(UserData(qur.userInfo.user_id, qur.userInfo.username, email, phone, party, "", "")), success = true)
      }else {
        Result(data = None, success = false)
      }
    }

    for {
      qur <- getPartyUser
      e = getEmail(qur)
      p = getPhone(qur)
      re <- update(qur, e, p)
    } yield getResult(re, qur, e, p)
  }

  //用户登录
  def getLoginUserInfo(userLogin: UserLogin): Future[UserGroupInfo] = {
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
      case e: BusinessException => Result[UserData](data = None, success = false, error = Some(Error(code = 111, message = e.message, field = "")))
    }
  }

  //管理员重置用户密码
  def adminResetUserPassword(username: String): Future[Result[String]] = {
    log.info(s"get into method adminResetUserPassword, username:${username}")

    val getInfo = getSpecificUserInfoByUsername(username)

    def getPartyUser(party: String, instanceId: String, userId: String): Future[QueryUserResult] = getSpecificPartyUser(party, instanceId, userId)

    val newPassword = "111111"
    def update(party: String, instanceId: String, userId: String, qur: QueryUserResult): Future[String] = {
      updatePartyUser(party, instanceId, userId, UserInfo(newPassword, qur.userInfo.phone, qur.userInfo.email, qur.userInfo.name, qur.userInfo.username).toJson.toString)
    }

    for {
      info <- getInfo
      qur <- getPartyUser(info.party, info.instanceId, info.userId)
      ur <- update(info.party, info.instanceId, info.userId, qur)
    } yield { Result(data = Some(ur), success = true)}
  }

  def getUserInfo(party: String, instance_id: String, userId: String): Future[QueryUserResult] = {
    getSpecificPartyUser(party, instance_id, userId)
  }

  def getUserInfoByUsername(username: String): Future[UserData] = {
    def deal(ugi: UserGroupInfo): UserData = {
      val role = if(ugi.gid.isDefined && ugi.gid.get == "2") ugi.party + "Accountant" else ugi.party
      UserData(userId = ugi.userId, username = ugi.userName, email = ugi.email, phone = ugi.phone, role = role, companyId = ugi.instanceId, companyName = ugi.companyName)
    }
    getSpecificUserInfoByUsername(username) map { deal(_)}
  }

  //管理员禁用用户
  def adminDisableUser(username: String): Future[Result[String]] = {
    log.info(s"get into method adminDisableUser, username:${username}")

    def getResult(result: String): Result[String] = {
      if(result == "success"){
        Result(data = Some(result))
      }else {
        Result(data = None, success = false)
      }
    }

    for {
      re: String <- disableUser(username)
    } yield getResult(re)
  }
}
