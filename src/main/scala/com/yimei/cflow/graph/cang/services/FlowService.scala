package com.yimei.cflow.graph.cang.services

import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserModelProtocol}
import com.yimei.cflow.graph.cang.models.CangFlowModel.TraffickerAssignUsers
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.api.util.HttpUtil._

import scala.concurrent.Future
/**
  * Created by wangqi on 16/12/28.
  */
object FlowService extends UserModelProtocol with Config{

  //完成选择港口,监管方和资金 -- 该操作只能由贸易方完成
  def submitA11(party_class:String,user_id:String,instant_id:String,tass:TraffickerAssignUsers) = {
    party_class match {
      case `myf` =>
        val gkUser = request[String,QueryUserResult](path="api/user", pathVariables = Array(gkf,tass.portCompanyId,tass.portUserId))
        val jgUser = request[String,QueryUserResult](path="api/user", pathVariables = Array(jgf,tass.supervisorCompanyId,tass.supervisorUserId))

       // val




        for {
          gk <- gkUser
          jg <- jgUser
        } yield {
          "success"
        }
      case _     => throw new BusinessException("hehhehe")
    }
  }

}
