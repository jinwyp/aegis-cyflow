package com.yimei.cflow.graph.cang.services

import akka.event.{Logging, LoggingAdapter}
import com.yimei.cflow.api.http.client.PartyClient
import com.yimei.cflow.api.http.client.UserClient
import com.yimei.cflow.api.util.HttpUtil._
import spray.json._
import DefaultJsonProtocol._
import com.yimei.cflow.api.http.models.PartyModel._
import com.yimei.cflow.api.http.models.ResultModel.{Error, Result}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.PartyInstanceEntity
import com.yimei.cflow.api.models.user.State
import com.yimei.cflow.graph.cang.exception.BusinessException

import scala.concurrent.{Await, Future, Promise}
import scala.concurrent.duration.Duration
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.models.UserModel.AddUser

//import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by xl on 16/12/26.
  */
object LoginService extends PartyClient with UserClient with Config with PartyModelProtocal{

  case class PartyClassEntity(id:Option[Long],class_name:String,description:String)

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
          cu <- createPartyUser(rzf, cpi.instance_id, userId, userInfo.toJson.toString)
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
      p success Result(data = res, success = true, error = null, meta = null)
    } ) recover {
      case e: BusinessException => {
        log.info(s"error happen, ${e.message}")
        p success  Result(data = State("", "", Map()), success = false, error = Error(code = 111, message = e.message, field =""), meta = null)
      }
    }

    p.future
  }
}
