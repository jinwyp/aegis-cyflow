package com.yimei.cflow.graph.cang.services

import com.yimei.cflow.api.http.models.TaskModel.{TaskProtocol, UserSubmitEntity}
import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserModelProtocol}
import com.yimei.cflow.api.models.database.FlowDBModel.FlowTaskEntity
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.{PartyInstanceEntity, UserGroupEntity}
import com.yimei.cflow.api.models.flow.{DataPoint, Graph}
import com.yimei.cflow.api.models.user.{State => UserState}
import com.yimei.cflow.api.util.HttpUtil._
import com.yimei.cflow.api.util.PointUtil._
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel.{FinancerToTrader, TraderRecommendAmount, TraffickerConfirmPayToFundProvider, _}
import spray.json._

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/28.
  */
object FlowService extends UserModelProtocol
  with TaskProtocol
  with Config {


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
          log.info("{}", uq.length)
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
  def getFlowData(flowId: String): Future[Graph] = {
    request[String, Graph](path = "api/flow", pathVariables = Array(flowId))
  }


  /**
    * 填充审批数据
    *
    * @param graph
    */
  def fillSPData(graph: Graph): SPData = {
    // val t: Option[DataPoint] =
    graph.state.get.points.get(startPoint) match {
      case Some(data) =>
        val startFlow: StartFlow = data.value.parseJson.convertTo[StartFlow]
        SPData(
          startFlow.basicInfo.financeCreateTime,
          startFlow.basicInfo.financeEndTime,
          "MID",
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
      case _ => throw BusinessException("flowId:" + graph.state.get.flowId + "没有初始数据")

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
      c<-company
      u<-user
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


  /**
    * 填充用户信息
    */
  def fillCYPartyMember(graph: Graph): Future[CYPartyMember] = {
    //根据guid获取用户信息
    def splitGUID(guid: String): (String, String, String) = {
      val regex = "([^-]+)-([^!])!(.*)".r
      guid match {
        case regex(party_class, instant_id, user_id) => (party_class, instant_id, user_id)
        case _ => throw BusinessException(s"$guid 有误，无法获得用户信息")
      }
    }

    //financer 数据
    val financerUser: UserInfo = graph.state.get.points.get(startPoint) match {
      case Some(data) =>
        val startFlow: StartFlow = data.value.parseJson.convertTo[StartFlow]
        UserInfo(startFlow.basicInfo.applyUserId,
          startFlow.basicInfo.applyUserPhone,
          Some(startFlow.basicInfo.applyUserPhone),
          None,
          startFlow.basicInfo.applyUserName,
          startFlow.basicInfo.applyCompanyName,
          startFlow.basicInfo.applyCompanyId
        )
      case _ => throw BusinessException("flowId:" + graph.state.get.flowId + "没有初始数据")
    }

    //harbor用户
    val harborUser: Future[Option[UserInfo]] = graph.state.get.points.get(harborUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1,splitedGuid._2,splitedGuid._3)
      case _ => Future{None}
    }

    //supervisor用户
    val supervisorUser: Future[Option[UserInfo]] = graph.state.get.points.get(supervisorUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1,splitedGuid._2,splitedGuid._3)
      case _ => Future{None}
    }

    //fundProvider用户
    val fundProviderUser: Future[Option[UserInfo]] = graph.state.get.points.get(fundProviderUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1,splitedGuid._2,splitedGuid._3)
      case _ => Future{None}
    }

    //fundProviderAccountantUser用户
    val fundProviderAccountantUser: Future[Option[UserInfo]] = graph.state.get.points.get(fundProviderAccountantUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1,splitedGuid._2,splitedGuid._3)
      case _ => Future{None}
    }

    //traderUser用户
    val traderUser: Future[Option[UserInfo]] = graph.state.get.points.get(traderUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1,splitedGuid._2,splitedGuid._3)
      case _ => Future{None}
    }

    //traderAccountantUser用户
    val traderAccountantUser: Future[Option[UserInfo]] = graph.state.get.points.get(traderAccountantUserId) match {
      case Some(data) =>
        val splitedGuid: (String, String, String) = splitGUID(data.value)
        getUserInfo(splitedGuid._1,splitedGuid._2,splitedGuid._3)
      case _ => Future{None}
    }

    for{
      hUser <- harborUser
      sUser <- supervisorUser
      fUser <- fundProviderUser
      faUser<- fundProviderAccountantUser
      tUser <- traderUser
      taUser<- traderAccountantUser
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
    *获得当前用户当前流程的任务
    */
  def getCurrentTasks(flowId:String,party_class: String, company_id: String, user_Id: String): Future[UserState] = {
    request[String,UserState](path = "api/utask",pathVariables = Array(party_class,company_id,user_Id)) map{ (ts: UserState) =>
      ts.copy(tasks = ts.tasks.filter(entry=>
        entry._2.flowId == flowId
      ))
    }
  }


  /**
    *还款记录（融资方任务历史）
    */
  def getRepayment(flowId:String, company_id: String, user_Id: String) = {
    val tasks: Future[Seq[FlowTaskEntity]] = request[String,Seq[FlowTaskEntity]](path="api/utask",pathVariables = Array(rzf,company_id,user_Id),
      paramters = Map("history"->"yes","flowId"->flowId,"taskname"->repaymentAmount))
  }



  def getFileObjects(fileNames:List[String]): Future[Seq[FileObj]] = {
    import com.yimei.cflow.asset.service.AssetService._
    getFiles(fileNames).map { sq =>
      sq.map(entity => FileObj(entity.url,entity.asset_id,entity.busi_type.toString))
    }

  }

  /**
    * 放货记录（港口方）
    */
  def getDeliverys(flowId:String, company_id: String, user_Id: String) = {
    request[String,Seq[FlowTaskEntity]](path="api/utask",pathVariables = Array(gkf,company_id,user_Id),
      paramters = Map("history"->"yes","flowId"->flowId,"taskname"->a20noticeHarborRelease)) map { (tasks: Seq[FlowTaskEntity]) =>
      tasks.map { entity =>
         entity.task_submit.parseJson.convertTo[Map[String,DataPoint]].get(traderNoticeHarborRelease) match {
           case Some(data) =>
             val delivery = data.value.parseJson.convertTo[TraffickerNoticePortReleaseGoods]
            // Delivery(delivery.redemptionAmount,data.timestamp,delivery.)
           case _          => throw BusinessException(s"flowId:$flowId, company_id:$company_id , user_id:$user_Id 港口放货记录有误")
         }



      }
    }



  }







}
