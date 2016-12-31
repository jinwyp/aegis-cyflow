package com.yimei.cflow.graph.cang.services

import com.yimei.cflow.api.http.models.TaskModel.{TaskProtocol, UserSubmitEntity}
import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserModelProtocol}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.{PartyInstanceEntity, UserGroupEntity}
import com.yimei.cflow.api.models.user.{State => UserState}
import com.yimei.cflow.api.util.HttpUtil._
import com.yimei.cflow.api.util.PointUtil._
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel.{TraderRecommendAmount, _}

import scala.concurrent.Future
/**
  * Created by wangqi on 16/12/28.
  */
object FlowService extends UserModelProtocol
  with TaskProtocol
  with Config {


  def genGuId(party_id:String, company_id:String, user_Id:String) = {
    party_id+"-"+company_id+"!"+user_Id
  }


//  def validateUserGroupRelation(party_class:String,user_id:String,instant_id:String,gid:String) = {
//    val company = request[String,Seq[PartyInstanceEntity]](path="api/inst", pathVariables = Array(party_class,instant_id)) map { t=>
//      t.length match {
//        case 1 => t(0)
//        case _ => throw new BusinessException(s"$party_class 类型,CompanyId: $instant_id 有多个方公司")
//      }
//  }


  /**
    * 贸易方完成选择港口,监管方和资金
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param tass
    * @return
    */
  def submitA11(party_class:String,user_id:String,instant_id:String,tass:TraffickerAssignUsers) = {
    genGuId(party_class,instant_id,user_id) match {
      case `myfUserId`  =>
        val gkUser = request[String,QueryUserResult](path="api/user", pathVariables = Array(gkf,tass.harborCompanyId,tass.harborUserId))
        val jgUser = request[String,QueryUserResult](path="api/user", pathVariables = Array(jgf,tass.supervisorCompanyId,tass.supervisorUserId))

        def zjfCompany: Future[PartyInstanceEntity] = {
          request[String,Seq[PartyInstanceEntity]](path="api/inst", pathVariables = Array(zjf,tass.fundProviderCompanyId)) map { t=>
            t.length match {
              case 1 => t(0)
              case _ => throw new BusinessException("CompanyId:"+tass.fundProviderCompanyId+" 有多个资金方公司")
            }

          }
        }

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

          val op = genGuId(myf,instant_id,user_id)

          //采集的数据点
          val points = Map(
            harborUserId     -> genGuId(gkf,tass.harborCompanyId,tass.harborUserId).wrap(operator = Some(op)),
            supervisorUserId -> genGuId(jgf,tass.supervisorCompanyId,tass.supervisorUserId).wrap(operator = Some(op)),
            fundProviderUserId -> genGuId(zjf,tass.fundProviderCompanyId,zjA.user_id).wrap(operator = Some(op)),
            fundProviderAccountantUserId -> genGuId(zjf,tass.fundProviderCompanyId,zjF.user_id).wrap(operator = Some(op))
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


  /**
    * 融资方上传文件或监管方上传文件
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param upload
    * @return
    */
  def submitA12AndA14(party_class:String,user_id:String,instant_id:String,taskName:String,upload:UploadContract) = {
    val dataPoint: String = (party_class,taskName) match {
      case (`rzf`,`a12FinishedUpload`) => financerContractFiles
      case (`jgf`,`a14FinishedUpload`) => supervisorContractFiles
      case _                           => throw new BusinessException(s"用户类型：$party_class 和任务 $taskName 不匹配")
    }

    val op = genGuId(party_class,instant_id,user_id)

    val points = Map( dataPoint -> upload.fileList.wrap(operator = Some(op)) )

    val userSubmit = UserSubmitEntity(upload.flowId,taskName,points)

    request[UserSubmitEntity,UserState](path="api/utask",pathVariables = Array(party_class,instant_id,user_id,upload.taskId),model = Some(userSubmit),method = "put")

  }

  /**
    * 港口方上传文件和确认吨数
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param harborUpload
    * @return
    */
  def submitA13(party_class:String,user_id:String,instant_id:String,taskName:String,harborUpload:HarborUploadContract) = {
    party_class match {
      case `gkf` =>
        val op = genGuId(party_class,instant_id,user_id)
        val points = Map(
          harborContractFiles -> harborUpload.fileList.wrap(operator = Some(op)),
          harborConfirmAmount -> harborUpload.confirmCoalAmount.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(harborUpload.flowId,taskName,points)
        request[UserSubmitEntity,UserState](path="api/utask",pathVariables = Array(party_class,instant_id,user_id,harborUpload.taskId),model = Some(userSubmit),method = "put")
      case  _    => throw new BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }

  /**
    *贸易方审核数据提交
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param audit
    * @return
    */
  def submitA15(party_class:String,user_id:String,instant_id:String,taskName:String,audit:TraderAudit) = {
    genGuId(party_class,instant_id,user_id) match {
      case `myfUserId` =>
        val op = genGuId(party_class,instant_id,user_id)
        val points = Map(
          traderAuditResult -> audit.status.wrap(operator = Some(op)),
          fundProviderInterestRate -> audit.fundProviderInterestRate.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(audit.flowId,taskName,points)
        request[UserSubmitEntity,UserState](path="api/utask",pathVariables = Array(party_class,instant_id,user_id,audit.taskId),model = Some(userSubmit),method = "put")
      case  _    => throw new BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 贸易方财务给出建议金额
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param recommend
    * @return
    */
  def submitA16(party_class:String,user_id:String,instant_id:String,taskName:String,recommend:TraderRecommendAmount) = {
    genGuId(party_class,instant_id,user_id) match {
      case `myfFinanceId` =>
        val op = genGuId(party_class,instant_id,user_id)
        val points = Map(
          recommendAmount -> recommend.recommendAmount.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(recommend.flowId,taskName,points)
        request[UserSubmitEntity,UserState](path="api/utask",pathVariables = Array(party_class,instant_id,user_id,recommend.taskId),model = Some(userSubmit),method = "put")
      case  _    => throw new BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }

  /**
    * 资金方审核
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param fundAudit
    * @return
    */
  def submitA17(party_class:String,user_id:String,instant_id:String,taskName:String,fundAudit:FundProviderAudit) = {
    party_class match {
      case `zjf` =>
        val op = genGuId(party_class,instant_id,user_id)
        val points = Map(
          fundProviderAuditResult -> fundAudit.status.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(fundAudit.flowId,taskName,points)
        request[UserSubmitEntity,UserState](path="api/utask",pathVariables = Array(party_class,instant_id,user_id,fundAudit.taskId),model = Some(userSubmit),method = "put")
      case  _    => throw new BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }







}
