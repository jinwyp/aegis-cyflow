package com.yimei.cflow.graph.cang.services

import java.sql.Timestamp
import java.util.concurrent.TimeUnit

import akka.stream.ThrottleMode
import akka.stream.scaladsl.Source
import com.yimei.cflow.api.http.models.AdminModel.{AdminProtocol, HijackEntity}
import com.yimei.cflow.api.http.models.TaskModel.{TaskProtocol, UserSubmitEntity}
import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserModelProtocol}
import com.yimei.cflow.api.models.database.FlowDBModel.FlowTaskEntity
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.{PartyInstanceEntity, UserGroupEntity}
import com.yimei.cflow.api.models.flow.{DataPoint, Graph, State => FlowState}
import com.yimei.cflow.api.models.user.{CommandUserTask, State => UserState}
import com.yimei.cflow.api.util.HttpUtil._
import com.yimei.cflow.api.util.PointUtil._
import com.yimei.cflow.asset.service.AssetService._
import com.yimei.cflow.engine.FlowRegistry
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel.{CYPartyMember, FinancerToTrader, TraderRecommendAmount, TraffickerConfirmPayToFundProvider, _}
import spray.json._
import com.yimei.cflow.config.CoreConfig._

import scala.concurrent.Future
import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.api.util.DBUtils._
import com.yimei.cflow.graph.cang.db.{CangPayTransactionTable, DepositTable}
import com.yimei.cflow.graph.cang.db.Entities.{CangPayTransactionEntity, DepositEntity}
import slick.backend.DatabasePublisher

import scala.concurrent.duration._

/**
  * Created by wangqi on 16/12/28.
  */
object FlowService extends UserModelProtocol
  with TaskProtocol
  with AdminProtocol
  with CangPayTransactionTable
  with DepositTable
  with Config {

  import driver.api._

  def genGuId(party_class: String, company_id: String, user_Id: String) = {
    party_class + "-" + company_id + "!" + user_Id
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
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param tass
    * @return
    */
  def submitA11(party_class: String, user_id: String, instant_id: String, tass: TraffickerAssignUsers) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfUserId` =>
        val gkUser = request[String, QueryUserResult](path = "api/user", pathVariables = Array(gkf, tass.harborCompanyId, tass.harborUserId))
        val jgUser = request[String, QueryUserResult](path = "api/user", pathVariables = Array(jgf, tass.supervisorCompanyId, tass.supervisorUserId))

        val zjUser: Future[UserGroupEntity] = request[String, Seq[UserGroupEntity]](path = "api/validateugroup", pathVariables = Array(zjf, tass.fundProviderCompanyId, tass.fundProviderUserId, fundGid)) map { uq =>
          uq.length match {
            case 1 => uq(0)
            case _ => throw BusinessException("CompanyId:" + tass.fundProviderCompanyId + "，userId:" + tass.fundProviderUserId + " 有多个资金方业务人员")
          }
        }

        val zjAccUser: Future[UserGroupEntity] = request[String, Seq[UserGroupEntity]](path = "api/validateugroup", pathVariables = Array(zjf, tass.fundProviderCompanyId, tass.fundProviderAccountantUserId, fundFinanceGid)) map (uq =>
          uq.length match {
            case 1 => uq(0)
            case _ => throw BusinessException("CompanyId:" + tass.fundProviderCompanyId + "，userId:" + tass.fundProviderUserId + " 有多个资金方财务务人员")
          }
          )

        //发送用户的提交
        def commitTask(gk: QueryUserResult, jg: QueryUserResult, zjA: UserGroupEntity, zjF: UserGroupEntity) = {

          val op = genGuId(myf, instant_id, user_id)

          //采集的数据点
          val points = Map(
            harborUserId -> genGuId(gkf, tass.harborCompanyId, tass.harborUserId).wrap(operator = Some(op)),
            supervisorUserId -> genGuId(jgf, tass.supervisorCompanyId, tass.supervisorUserId).wrap(operator = Some(op)),
            fundProviderUserId -> genGuId(zjf, tass.fundProviderCompanyId, zjA.user_id).wrap(operator = Some(op)),
            fundProviderAccountantUserId -> genGuId(zjf, tass.fundProviderCompanyId, zjF.user_id).wrap(operator = Some(op))
          )
          val userSubmit = UserSubmitEntity(tass.flowId, a11SelectHarborAndSupervisor, points)
          request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, tass.taskId), model = Some(userSubmit), method = "put")
        }

        for {
          gk <- gkUser
          jg <- jgUser
          zjAccount <- zjUser
          zjFinanceAccount <- zjAccUser
          r <- commitTask(gk, jg, zjAccount, zjFinanceAccount)
        } yield {
          r
        }
      case _ => throw BusinessException("该用户:" + genGuId(party_class, instant_id, user_id) + "没有提交" + a11SelectHarborAndSupervisor + "任务的权限")
    }
  }


  /**
    * 融资方上传文件或监管方上传文件
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param upload
    * @return
    */
  def submitA12AndA14(party_class: String, user_id: String, instant_id: String, taskName: String, upload: UploadContract) = {
    val dataPoint: String = (party_class, taskName) match {
      case (`rzf`, `a12FinishedUpload`) => financerContractFiles
      case (`jgf`, `a14FinishedUpload`) => supervisorContractFiles
      case _ => throw new BusinessException(s"用户类型：$party_class 和任务 $taskName 不匹配")
    }

    val op = genGuId(party_class, instant_id, user_id)

    val points = Map(dataPoint -> upload.fileList.wrap(operator = Some(op)))

    val userSubmit = UserSubmitEntity(upload.flowId, taskName, points)

    request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, upload.taskId), model = Some(userSubmit), method = "put")

  }

  /**
    * 港口方上传文件和确认吨数
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param harborUpload
    * @return
    */
  def submitA13(party_class: String, user_id: String, instant_id: String, taskName: String, harborUpload: HarborUploadContract) = {
    party_class match {
      case `gkf` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          harborContractFiles -> harborUpload.fileList.wrap(operator = Some(op)),
          harborConfirmAmount -> harborUpload.harborConfirmAmount.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(harborUpload.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, harborUpload.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }

  /**
    * 贸易方审核数据提交
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param audit
    * @return
    */
  def submitA15(party_class: String, user_id: String, instant_id: String, taskName: String, audit: TraderAudit) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfUserId` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          traderAuditResult -> audit.approvedStatus.wrap(operator = Some(op)),
          fundProviderInterestRate -> audit.fundProviderInterestRate.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(audit.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, audit.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 贸易方财务给出建议金额
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param recommend
    * @return
    */
  def submitA16(party_class: String, user_id: String, instant_id: String, taskName: String, recommend: TraderRecommendAmount) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfFinanceId` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          recommendAmount -> recommend.loanValue.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(recommend.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, recommend.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }

  /**
    * 资金方审核
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param fundAudit
    * @return
    */
  def submitA17(party_class: String, user_id: String, instant_id: String, taskName: String, fundAudit: FundProviderAudit) = {
    party_class match {
      case `zjf` =>
        val valid: Future[UserGroupEntity] = request[String, Seq[UserGroupEntity]](path = "api/validateugroup", pathVariables = Array(party_class, instant_id, user_id, fundGid)) map { t =>
          t.length match {
            case 1 => t(0)
            case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 公司： $instant_id 有误")
          }
        }

        def submit(v: UserGroupEntity): Future[UserState] = {
          //这里仅仅为了控制执行顺序。
          val op = genGuId(party_class, instant_id, v.user_id)
          val points = Map(
            fundProviderAuditResult -> fundAudit.approvedStatus.wrap(operator = Some(op))
          )
          val userSubmit = UserSubmitEntity(fundAudit.flowId, taskName, points)
          request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, fundAudit.taskId), model = Some(userSubmit), method = "put")
        }

        for {
          v <- valid
          r <- submit(v)
        } yield r

      case _ => throw new BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 资金方财务审核
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param fundAudit
    * @return
    */
  def submitA18(party_class: String, user_id: String, instant_id: String, taskName: String, fundAudit: FundProviderAccountantAudit) = {
    party_class match {
      case `zjf` =>
        val valid: Future[UserGroupEntity] = request[String, Seq[UserGroupEntity]](path = "api/validateugroup", pathVariables = Array(party_class, instant_id, user_id, fundFinanceGid)) map { t =>
          t.length match {
            case 1 => t(0)
            case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 公司： $instant_id 有误")
          }
        }

        def submit(v: UserGroupEntity): Future[UserState] = {
          //这里仅仅为了控制执行顺序。
          val op = genGuId(party_class, instant_id, v.user_id)
          val points = Map(
            fundProviderAccountantAuditResult -> fundAudit.status.wrap(operator = Some(op))
          )
          val userSubmit = UserSubmitEntity(fundAudit.flowId, taskName, points)
          request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, fundAudit.taskId), model = Some(userSubmit), method = "put")
        }

        for {
          v <- valid
          r <- submit(v)
        } yield r

      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 融资方确认回款
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param ft
    * @return
    */
  def submitA19(party_class: String, user_id: String, instant_id: String, taskName: String, ft: FinancerToTrader) = {
    party_class match {
      case `rzf` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          repaymentAmount -> ft.repaymentValue.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(ft.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, ft.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 贸易方通知港口方放货
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param release
    * @return
    */
  def submitA20(party_class: String, user_id: String, instant_id: String, taskName: String, release: TraffickerNoticePortReleaseGoods) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfUserId` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          traderNoticeHarborRelease -> release.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(release.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, release.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 港口方放货
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param release
    * @return
    */
  def submitA21(party_class: String, user_id: String, instant_id: String, taskName: String, release: PortReleaseGoods) = {
    party_class match {
      case `gkf` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          harborReleaseGoods -> release.status.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(release.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, release.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 贸易商确认是否还款完成
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param cpl
    * @return
    */
  def submitA22(party_class: String, user_id: String, instant_id: String, taskName: String, cpl: TraffickerAuditIfCompletePayment) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfUserId` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          TraderAuditIfCompletePayment -> cpl.status.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(cpl.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, cpl.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }

  /**
    * 贸易商确认回款给资金方
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param cf
    * @return
    */
  def submitA23(party_class: String, user_id: String, instant_id: String, taskName: String, cf: TraffickerConfirmPayToFundProvider) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfUserId` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          TraderConfirmPayToFundProvider -> cf.status.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(cf.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, cf.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }


  /**
    * 贸易商财务确认回款给资金方
    *
    * @param party_class
    * @param user_id
    * @param instant_id
    * @param taskName
    * @param cf
    * @return
    */
  def submitA24(party_class: String, user_id: String, instant_id: String, taskName: String, cf: TraffickerFinancePayToFundProvider) = {
    genGuId(party_class, instant_id, user_id) match {
      case `myfFinanceId` =>
        val op = genGuId(party_class, instant_id, user_id)
        val points = Map(
          TraderAccountantConfirm -> cf.status.wrap(operator = Some(op))
        )
        val userSubmit = UserSubmitEntity(cf.flowId, taskName, points)
        request[UserSubmitEntity, UserState](path = "api/utask", pathVariables = Array(party_class, instant_id, user_id, cf.taskId), model = Some(userSubmit), method = "put")
      case _ => throw BusinessException(s"用户: $user_id  类型：$party_class 和任务 $taskName 不匹配")
    }
  }

  /**
    * 根据flowId查询流程
    *
    * @param flowId
    * @return
    */
  def getFlowData(flowId: String): Future[FlowState] = {
    request[String, FlowState](path = "api/flow/state", pathVariables = Array(flowId))
  }


  /**
    * 填充审批数据
    *
    * @param state
    */
  def fillSPData(state: FlowState): SPData = {
    // val t: Option[DataPoint] =
    state.points.get(startPoint) match {
      case Some(data) =>
        val startFlow: StartFlow = data.value.parseJson.convertTo[StartFlow]
        SPData(
          startFlow.basicInfo.financeCreateTime,
          startFlow.basicInfo.financeEndTime,
          "MYD",
          startFlow.basicInfo.businessCode,
          startFlow.basicInfo.downstreamCompanyName,
          startFlow.basicInfo.stockPort,
          startFlow.basicInfo.coalAmount,
          startFlow.basicInfo.financingAmount,
          startFlow.basicInfo.financingDays,
          startFlow.basicInfo.interestRate,
          startFlow.basicInfo.coalType,
          startFlow.basicInfo.coalIndex_NCV,
          startFlow.basicInfo.coalIndex_RS,
          startFlow.basicInfo.coalIndex_ADV,
          startFlow.investigationInfo,
          startFlow.supervisorInfo
        )
      case _ => throw BusinessException("flowId:" + state.flowId + "没有初始数据")

    }
  }


  //获取用户信息
  def getUserInfo(party_class: String, instant_id: String, user_id: String): Future[Some[UserInfo]] = {

    //获得公司
    val company: Future[PartyInstanceEntity] = request[String, Seq[PartyInstanceEntity]](path = "api/inst", pathVariables = Array(party_class, instant_id)) map { t =>
      t.length match {
        case 1 => t(0)
        case _ => throw new BusinessException(s"$party_class 类型,CompanyId: $instant_id 有多个方公司")
      }
    }
    //获得用户信息
    val user: Future[QueryUserResult] = request[String, QueryUserResult](path = "api/user", pathVariables = Array(party_class, instant_id, user_id))

    for {
      c <- company
      u <- user
    } yield {
      Some(UserInfo(u.userInfo.user_id,
        u.userInfo.username,
        u.userInfo.phone,
        u.userInfo.email,
        u.userInfo.name,
        c.companyName,
        c.instanceId))
    }
  }


  //根据guid获取用户信息
  private def splitGUID(guid: String): (String, String, String) = {
    val regex = "([^-]+)-([^!]+)!(.*)".r
    guid match {
      case regex(party_class, instant_id, user_id) => (party_class, instant_id, user_id)
      case _ => throw BusinessException(s"$guid 有误，无法获得用户信息")
    }
  }


  /**
    * 填充用户信息
    */
  def fillCYPartyMember(state: FlowState): Future[CYPartyMember] = {
    //financer 数据
    val financerUser: UserInfo = state.points.get(startPoint) match {
      case Some(data) =>
        val startFlow: StartFlow = data.value.parseJson.convertTo[StartFlow]
        UserInfo(startFlow.basicInfo.applyUserId,
          startFlow.basicInfo.applyUserPhone,
          Some(startFlow.basicInfo.applyUserPhone),
          None,
          startFlow.basicInfo.applyUserName.get,
          startFlow.basicInfo.applyCompanyName,
          startFlow.basicInfo.applyCompanyId
        )
      case _ => throw BusinessException("flowId:" + state.flowId + "没有初始数据")
    }

    //harbor用户
    val harborUser: Future[Option[UserInfo]] = state.points.get(harborUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1, splitedGuid._2, splitedGuid._3)
      case _ => Future {
        None
      }
    }

    //supervisor用户
    val supervisorUser: Future[Option[UserInfo]] = state.points.get(supervisorUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1, splitedGuid._2, splitedGuid._3)
      case _ => Future {
        None
      }
    }

    //fundProvider用户
    val fundProviderUser: Future[Option[UserInfo]] = state.points.get(fundProviderUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1, splitedGuid._2, splitedGuid._3)
      case _ => Future {
        None
      }
    }

    //fundProviderAccountantUser用户
    val fundProviderAccountantUser: Future[Option[UserInfo]] = state.points.get(fundProviderAccountantUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1, splitedGuid._2, splitedGuid._3)
      case _ => Future {
        None
      }
    }

    //traderUser用户
    val traderUser: Future[Option[UserInfo]] = state.points.get(traderUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1, splitedGuid._2, splitedGuid._3)
      case _ => Future {
        None
      }
    }

    //traderAccountantUser用户
    val traderAccountantUser: Future[Option[UserInfo]] = state.points.get(traderAccountantUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1, splitedGuid._2, splitedGuid._3)
      case _ => Future {
        None
      }
    }

    for {
      hUser <- harborUser
      sUser <- supervisorUser
      fUser <- fundProviderUser
      faUser <- fundProviderAccountantUser
      tUser <- traderUser
      taUser <- traderAccountantUser
    } yield {
      CYPartyMember(
        harbor = hUser,
        supervisor = sUser,
        fundProvider = fUser,
        fundProviderAccountant = faUser,
        trader = tUser,
        traderAccountant = taUser,
        financer = financerUser
      )
    }
  }


  /**
    * 获得当前用户当前流程的任务
    */
  def getCurrentTasks(flowId: String, party_class: String, company_id: String, user_Id: String): Future[UserState] = {
    request[String, UserState](path = "api/utask", pathVariables = Array(party_class, company_id, user_Id)) map { (ts: UserState) =>
      ts.copy(tasks = ts.tasks.filter(entry =>
        entry._2.flowId == flowId
      ))
    }
  }


  def getFileObjects(fileNames: List[String]): Future[Seq[FileObj]] = {

    getFiles(fileNames).map { sq =>
      sq.map(entity => FileObj(entity.asset_id, entity.origin_name, entity.busi_type))
    }

  }

  /**
    * 放货记录（港口方）
    */
  def getDeliverys(flowId: String, cyPartyMember: CYPartyMember): Future[(Option[List[Delivery]], Option[BigDecimal])] = {

    cyPartyMember.trader match {
      case Some(trader) =>
        request[String, Seq[FlowTaskEntity]](path = "api/utask", pathVariables = Array(myf, trader.companyId, trader.userId),
          paramters = Map("history" -> "yes", "flowId" -> flowId, "taskname" -> a20noticeHarborRelease)) flatMap { (tasks: Seq[FlowTaskEntity]) =>
          tasks.length match {
            case 0 => Future {
              (None, None)
            }
            case _ =>
              var totalRedemptionAmount = BigDecimal(0)
              Future.sequence(tasks.map { entity =>
                entity.task_submit.parseJson.convertTo[Map[String, DataPoint]].get(traderNoticeHarborRelease) match {
                  case Some(data) =>
                    val delivery = data.value.parseJson.convertTo[TraffickerNoticePortReleaseGoods]
                    getFileObjects(delivery.fileList) map { (fs: Seq[FileObj]) =>
                      totalRedemptionAmount = totalRedemptionAmount + delivery.redemptionAmount
                      Delivery(delivery.redemptionAmount, new Timestamp(data.timestamp), fs.toList, data.operator, delivery.goodsReceiveCompanyName)
                    }
                  case _ => throw BusinessException(s"flowId:$flowId, company_id:${trader.companyId} , user_id:${trader.userId} 港口放货记录有误")
                }
              }) map { sq =>
                (Some(sq.toList), Some(totalRedemptionAmount))
              }
          }
        }
      case _ =>
        Future {
          (None, None)
        }
    }


  }


  /**
    * 获取流程中全部文件记录
    */
  def getFileList(state: FlowState): List[String] = {
    def getList(names: Option[DataPoint]): List[String] = {
      names match {
        case Some(d) => d.value.parseJson.convertTo[List[String]]
        case _ => List[String]()
      }
    }

    getList(state.points.get(financerContractFiles)) ::: getList(state.points.get(harborContractFiles)) ::: getList(state.points.get(supervisorContractFiles))
  }


  /**
    * 还款记录（融资方任务历史）
    */
  def getRepayment(flowId: String, company_id: String, user_Id: String): Future[Seq[FlowTaskEntity]] = {
    request[String, Seq[FlowTaskEntity]](path = "api/utask", pathVariables = Array(rzf, company_id, user_Id),
      paramters = Map("history" -> "yes", "flowId" -> flowId, "taskname" -> a19SecondReturnMoney))
  }

  /**
    * 还款记录 (差融资方的记录)
    *
    * @param flowId
    * @param company_id
    * @param user_Id
    * @param interest 利率
    * @param state
    * @return (实际放款金额，已还款金额，未还款金额，还款记录)
    */
  def calculateInterest(flowId: String, cyPartyMember: CYPartyMember, interest: BigDecimal, state: FlowState): Future[(Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[List[Repayment]])] = {

    val financer = cyPartyMember.financer

    state.points.get(recommendAmount) match {
      //说明贸易方已经审核通过了。已经有了借款金额
      case Some(total) =>
        state.points.get(financerPaySuccess) match {
          //说明融资方至少有一笔还款了
          case Some(_) =>

            //这个地方肯定是有了，因为已经有还款了
            val startDate: Long = state.points(traderPaySuccess).timestamp
            //拿到全部的还款的task
            val tasks: Future[Seq[FlowTaskEntity]] = getRepayment(flowId, financer.companyId, financer.userId)
            val tt: BigDecimal = BigDecimal(total.value.toDouble)
            var curMoney = tt

            //获得还款记录表
            def getRepaymentList(tasks: Seq[FlowTaskEntity]): Seq[Repayment] = {
              tasks.map { t =>
                t.task_submit.parseJson.convertTo[Map[String, DataPoint]].get(repaymentAmount) match {
                  case Some(data) =>
                    //本次还款金额
                    val repaymentValue = BigDecimal(data.value.toDouble)
                    val days: Long = TimeUnit.MICROSECONDS.toDays(data.timestamp - startDate) + 1
                    val result = Repayment(
                      repaymentValue,
                      curMoney,
                      curMoney - repaymentValue,
                      days,
                      curMoney * days * interest / 365
                    )
                    curMoney = curMoney - repaymentValue
                    result
                  case _ => throw BusinessException(s"$flowId 交易流水异常")
                }
              }
            }

            for {
              ts <- getRepayment(flowId, financer.companyId, financer.userId)
              repayments = getRepaymentList(ts)
            } yield {
              (Some(tt), Some(tt - curMoney), Some(curMoney), Some(repayments.toList))
            }
          case _ => Future {
            (Some(BigDecimal(total.value.toDouble)), None, None, None)
          }
        }
      case _ => Future {
        (None, None, None, None)
      }
    }
  }


  //帮助函数
  private def extractValue(key: String, state: FlowState): Option[BigDecimal] = {
    state.points.get(key) match {
      case Some(data) => Some(BigDecimal(data.value))
      case _ => None
    }
  }

  //获取保证金金额记录
  def getDepositList(flowId: String): Future[List[DepositRecord]] = dbrun(deposit.filter{ dpt => dpt.state === TRANSFERRED && dpt.flowId === flowId}.result) map { dplist =>
    dplist.map{ dp => DepositRecord(expectedAmount = dp.expectedAmount,
      actuallyAmount = dp.actuallyAmount,
      memo = dp.memo,
      status = dp.state,
      ts_c = dp.ts_c.get)}.toList
  }


  //获取保证金总额
  def getDepositAmount(flowId: String): Future[BigDecimal] = {
    getDepositList(flowId).map { list =>
      list.foldLeft(BigDecimal(0))((sum, dp) => sum + dp.actuallyAmount)
    }
  }


  /**
    * 组装flow数据
    *
    * @param state         当前流程状态
    * @param currentTask   当前用户在该流程的任务
    * @param Filelist      流程对应全部的文件
    * @param deliveryInfo  港口放货信息（放货清单，放货总量）
    * @param repaymentInfo 还款记录（实际放款金额，已还款金额，未还款金额，还款清单）
    */
  def setFlowData(state: FlowState,
                  currentTask: UserState,
                  Filelist: Seq[FileObj],
                  deliveryInfo: (Option[List[Delivery]], Option[BigDecimal]),
                  repaymentInfo: (Option[BigDecimal], Option[BigDecimal], Option[BigDecimal], Option[List[Repayment]]),
                  depositAmount: BigDecimal,
                  depositList: List[DepositRecord]): FlowData = {

    //货权（贸易商审核通过前为融资方，然后为贸易方
    val cargoOwner = state.histories.contains(E3) match {
      case true => myf
      case false => rzf
    }

    val graph = FlowRegistry.flowGraph(flowType)

    //港口确认金额
    val harborCA: Option[BigDecimal] = extractValue(harborConfirmAmount, state)


    //待赎回吨数
    val redemptionAmountLeft: Option[BigDecimal] = (harborCA, deliveryInfo._2) match {
      case (Some(ca), Some(da)) => Some(ca - da) //港口确认吨数 - 实际赎回吨数
      case (Some(ca), _) => Some(ca) //港口确认吨数（此时还没有还款赎回）
      case _ => None //港口还没有确认金额的时候
    }

    val states = state.edges.map(entry =>
      graph.edges(entry._1).begin
    ).toList




    FlowData(
      currentTask, //当前任务
      cargoOwner, //货权（贸易商审核通过前为融资方，然后为贸易方）
      states.length match {
        case 1 => states(0)
        case 0 => ""
        case _ => throw BusinessException(s"$states 异常")
      }, //当前所在vertices
      repaymentInfo._1, // 实际放款金额
      Some(depositAmount),
      extractValue(fundProviderInterestRate, state), //资金方借款的利率
      harborCA, //港口确认吨数
      deliveryInfo._2, //已赎回吨数
      repaymentInfo._2, //已归还金额
      redemptionAmountLeft, //待赎回吨数
      repaymentInfo._3, //待还款
      Some(depositList),
      repaymentInfo._4, //还款交易记录
      deliveryInfo._1, //放货记录
      Filelist.toList //该流程对应全部文件
    )
  }

  private def getTaskInfo(us: UserState): (String, String) = {
    val taskList: List[(String, CommandUserTask)] = us.tasks.toList
    taskList.length match {
      case 1 => (taskList(0)._1, taskList(0)._2.taskName)
      case 0 => ("", "")
      case _ => throw BusinessException(s"userType:${us.userType}, userId:${us.userId}有误")
    }
  }

  /**
    * 组装流程数据
    */
  def cyDataCollection(flowId: String, party_class: String, company_id: String, user_Id: String) = {


    for {
    //获取流程数据
      flowState <- getFlowData(flowId)
      //审批带来的数据
      spData = fillSPData(flowState)
      //流程中文件列表
      fileList <- getFileObjects(getFileList(flowState))
      //仓压成员记录
      cyPartyMember <- fillCYPartyMember(flowState)
      //港口放货记录
      deliverys <- getDeliverys(flowId, cyPartyMember)
      //用户当前任务
      currentTask <- getCurrentTasks(flowId, party_class, company_id, user_Id)
      //融资方还款记录
      repayments <- calculateInterest(flowId, cyPartyMember, spData.interestRate, flowState)
      taskInfo = getTaskInfo(currentTask)
      depositList <- getDepositList(flowState.flowId)
      depositAmount <- getDepositAmount(flowState.flowId)
    } yield {
      CYData(spData,
        cyPartyMember,
        setFlowData(flowState, currentTask, fileList, deliverys, repayments,depositAmount, depositList),
        flowId,
        taskInfo._1,
        taskInfo._2
      )
    }


  }

  /**
    * 插入交易记录
    * @param srcGuid
    * @param targetGuid
    * @param amount
    * @param flowId
    * @param pointName
    * @return
    */
  def insertIntoCangPay(srcGuid:String,targetGuid:String,amount:BigDecimal,flowId:String,pointName:String) = {

    val src = splitGUID(srcGuid)
    val target = splitGUID(targetGuid)
    val cpt = CangPayTransactionEntity(
      None,
      flowId,
      pointName,
      src._1,
      src._3,
      src._2,
      target._1,
      target._3,
      target._2,
      amount,
      None,
      1,
      None
    )

    val cp: Future[CangPayTransactionEntity] = dbrun(
      cangPayTransaction returning cangPayTransaction.map(_.id) into ( (cp,id) => cp.copy(id=id) ) += cpt
    )

    def req(cp:CangPayTransactionEntity): Future[PayResponse] = requestServer[PayRequest,PayResponse](
      path = "pay/transfer/account",
      model = Some(PayRequest(
        cp.srcUserType,
        cp.srcCompanyId,
        cp.targetUserType,
        cp.targetCompanyId,
        cp.amount)),
      method="post"
    )

    def update(resp:PayResponse,id:Option[Long]): Future[Int] = {
      dbrun(cangPayTransaction.filter(_.id===id)
        .map(c => (c.status,c.transactionId,c.message))
        .update((resp.status,resp.transactionId,resp.message)))
    }

    for{
      p <- cp                                       //插入一条打款记录
      r <- req(p)                                   //发起打款
      i <- update(r,p.id)                           //根据返回更新数据
    } yield {
      i
    }
  }


  /**
    * 跑批查询
    * @return
    */
  def queryPayResult() = {
    val queryList: DatabasePublisher[CangPayTransactionEntity] = db.stream(
      cangPayTransaction.filter(c=>c.status===process).result
    )

    Source.fromPublisher(queryList).throttle(1, 5 seconds, 1, ThrottleMode.shaping)
      .runForeach(b => {
        val res = requestServer[String,PayQueryResponse](path="pay/transaction/query",paramters = Map("transactionId"->(b.transactionId.getOrElse("error"))))

        res.map{ re =>
          re.status match {
            case 2 =>
              //成功
              dbrun(cangPayTransaction.filter(_.transactionId === b.transactionId)
                .map(c => (c.status,c.message))
                .update((re.status,re.message))) map { i =>
                //填充point
                val point = Map(b.pointName -> "success".wrap())
                val hijackEntity = HijackEntity(updatePoints = point, trigger = true, decision = None)
                request[HijackEntity, FlowState](path = "api/flow/admin/hijack", pathVariables = Array(b.flowId), model = Some(hijackEntity), method = "put")
              }
            case 0 =>
              //失败
              dbrun(cangPayTransaction.filter(_.transactionId === b.transactionId)
                .map(c => (c.status,c.message))
                .update((re.status,re.message)))
            case _ =>
          }

        }

      })



  }



}
