package com.yimei.cflow.graph.cang.services

import com.yimei.cflow.api.http.models.TaskModel.{TaskProtocol, UserSubmitEntity}
import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserModelProtocol}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.{PartyInstanceEntity, UserGroupEntity}
import com.yimei.cflow.api.models.user.{State => UserState}
import com.yimei.cflow.api.util.HttpUtil._
import com.yimei.cflow.api.util.PointUtil._
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel.TraffickerAssignUsers
/**
  * Created by wangqi on 16/12/28.
  */
object FlowService extends UserModelProtocol
  with TaskProtocol
  with Config {


  def genGuId(party_id:String, company_id:String, user_Id:String) = {
    party_id+"_"+company_id+"!"+user_Id
  }



  //完成选择港口,监管方和资金 -- 该操作只能由贸易方完成
  def submitA11(party_class:String,user_id:String,instant_id:String,tass:TraffickerAssignUsers) = {
    party_class match {
      case `myf` =>
        val gkUser = request[String,QueryUserResult](path="api/user", pathVariables = Array(gkf,tass.harborCompanyId,tass.harborUserId))
        val jgUser = request[String,QueryUserResult](path="api/user", pathVariables = Array(jgf,tass.supervisorCompanyId,tass.supervisorUserId))

        val zjfCompany = request[String,PartyInstanceEntity](path="api/inst", pathVariables = Array(zjf,tass.fundProviderCompanyId))

        //获取指定资金方的指定组的账户
        def getzjfAccount(party_id:String,gid:String) = {
          request[String,Seq[UserGroupEntity]](path="api/ugroup", pathVariables = Array(party_id,gid)) map { result =>
              result.length match {
                case 1 => result(0)
                case _ => throw new BusinessException("资金方分组错误")
              }
          }
        }

        //发送用户的提交
        def commitTask(gk:QueryUserResult,jg:QueryUserResult,zjA:UserGroupEntity,zjF:UserGroupEntity) = {
          //采集的数据点
          val points = Map(
            harborUserId     -> genGuId(gkf,tass.harborCompanyId,tass.harborUserId).wrap(),
            supervisorUserId -> genGuId(jgf,tass.supervisorCompanyId,tass.supervisorUserId).wrap(),
            fundProviderUserId -> genGuId(zjf,tass.fundProviderCompanyId,zjA.user_id).wrap(),
            fundProviderAccountantUserId -> genGuId(zjf,tass.fundProviderCompanyId,zjF.user_id).wrap()
          )
          val userSubmit = UserSubmitEntity(tass.flowId,a11SelectHarborAndSupervisor,points)
          request[UserSubmitEntity,UserState](path="api/utask",pathVariables = Array(party_class,instant_id,user_id,tass.taskId),model = Some(userSubmit),method = "put")
        }

        for {
          gk <- gkUser
          jg <- jgUser
          zjc <- zjfCompany
          zjAccount <- getzjfAccount(zjc.id.get.toString,fundGid)
          zjFinanceAccount <- getzjfAccount(zjc.id.get.toString,fundFinanceGid)
          r <- commitTask(gk,jg,zjAccount,zjFinanceAccount)
        } yield {
          r
        }
      case _     => throw new BusinessException("该用户:"+genGuId(party_class,instant_id,user_id)+"没有提交"+a11SelectHarborAndSupervisor+"任务的权限")
    }
  }

}
