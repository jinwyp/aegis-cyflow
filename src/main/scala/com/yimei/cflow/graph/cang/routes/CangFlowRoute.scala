package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.http.client.AdminClient
import com.yimei.cflow.api.http.models.AdminModel.{AdminProtocol, HijackEntity}
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.api.http.models.PartyModel.PartyInstanceInfo
import com.yimei.cflow.graph.cang.config.Config
import spray.json._
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserAddModel, UserModelProtocol}
import com.yimei.cflow.api.models.database.FlowDBModel.FlowInstanceEntity
import com.yimei.cflow.api.models.flow.State
import com.yimei.cflow.api.util.HttpUtil.request
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel._
import com.yimei.cflow.graph.cang.services.FlowService._
import com.yimei.cflow.graph.cang.session.{MySession, Session, SessionProtocol}
import spray.json._
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.graph.cang.services.LoginService._

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/26.
  * 流程相关路由
  */
class CangFlowRoute extends AdminClient
  with AdminProtocol
  with SprayJsonSupport
  with ResultProtocol
  with Session
  with SessionProtocol
  with UserModelProtocol
  with Config {

  ///////////////////////////////////////////////融资方////////////呵呵呵呵呵/////////////////////////////////////////////////////////////////////

  /**
    * 初始化
    *
    * @return
    */
  def startFlow = post {
    pathPrefix("startflow") {
      entity(as[StartFlow]) { startFlow =>
        import com.yimei.cflow.graph.cang.services.LoginService
        val getExistUserInfo: Future[QueryUserResult] = LoginService.getUserInfo(rzf, startFlow.basicInfo.applyCompanyId, startFlow.basicInfo.applyUserId)

        (for {
          existUserInfo <- getExistUserInfo
        } yield existUserInfo.userInfo.id) recover {
          case _ => {
            createPartyInstance(PartyInstanceInfo(rzf, startFlow.basicInfo.applyCompanyId, startFlow.basicInfo.applyCompanyName).toJson.toString)
            val user = UserAddModel(startFlow.basicInfo.applyUserPhone, "111111", startFlow.basicInfo.applyUserName.getOrElse(""), "", startFlow.basicInfo.applyUserPhone, startFlow.basicInfo.applyCompanyId, rzf)
            createPartyUser(rzf, startFlow.basicInfo.applyCompanyId, startFlow.basicInfo.applyUserId, user.toJson.toString)
          }
        }

        complete(createFlow(rzf, startFlow.basicInfo.applyCompanyId.toString, startFlow.basicInfo.applyUserId.toString, flowType,
          Map(startPoint -> startFlow.toJson.toString,
            orderId -> startFlow.basicInfo.businessCode,
            traderUserId -> myfUserId,
            traderAccountantUserId -> myfFinanceId)
        ) map { c =>
          StartFlowResult(true)
        })
      }
    }
  }

  /**
    *
    * @return
    */
  def submitFinancerTask = post {
    path("internal"/"financerTask" / Segment / Segment / Segment) { (action, user_id, instance_id) =>
      action match {
        case `a12FinishedUpload` =>
          entity(as[UploadContract]) { upload =>
            complete(submitA12AndA14(rzf, user_id, instance_id, action, upload))
          }
        case `a19SecondReturnMoney` =>
          entity(as[FinancerToTrader]) { repayment =>
            complete(submitA19(rzf, user_id, instance_id, action, repayment))
          }
        case _ => throw BusinessException(s"$action 不支持的任务类型")
      }
    }
  }


  /**
    * 融资方flow列表
    *
    * @return
    */
  def financerFlowList = get {
    path("internal"/"financerFlowList" / Segment / Segment) { (user_id, instance_id) =>
      complete(getFinancerList(instance_id, user_id))
    }
  }


  def financerflowDetail = get {
    pathPrefix("internal"/"financerDetail" / Segment / Segment / Segment) {(user_id, instance_id, flowId) =>
   // (path("financeorders" / Segment) & myRequiredSession) { (flowId, session) =>
//      val party_class = session.party
//      val user_id = session.userId
//      val instance_id = session.instanceId
      //获得流程信息
      complete(cyDataResult(flowId, rzf , instance_id, user_id))
    }
  }



  ////////////////////////////////////////////别的////////////////////////////////////////////////////////////////////////////////////

  //todo 改成session
  def flowList = get {
    //path("financeorders" / Segment / Segment / Segment) { (classType, companyId, userId) =>
    (path("financeorders") & myRequiredSession) { (session: MySession) =>
      val classType = session.party
      val userId = session.userId
      val companyId = session.instanceId

      complete(classType match {
        case `rzf` => getFinancerList(companyId, userId)
        case _     => getflowList(classType, companyId, userId)
      })

    //  complete(getflowList(classType, companyId, userId))
    }
  }


  /**
    * 提交任务
    *
    * @return
    */
  def submitTask = post {
    // path("financeorders" / "action" / Segment) { action =>
   // path("financeorders" / "action" / Segment / Segment / Segment / Segment) { (action, user_id, party_class, instance_id) =>
    (path("financeorders" / "action" / Segment) & myRequiredSession) { (action, session) =>
      val party_class = session.party
      val user_id = session.userId
      val instance_id = session.instanceId
      action match {
        //完成选择港口,监管方和资金方
        case `a11SelectHarborAndSupervisor` =>
          entity(as[TraffickerAssignUsers]) { tAssign =>
            complete(submitA11(party_class, user_id, instance_id, tAssign))
          }
        //融资方上传文件或监管方上传文件
        case `a12FinishedUpload` | `a14FinishedUpload` =>
          entity(as[UploadContract]) { upload =>
            complete(submitA12AndA14(party_class, user_id, instance_id, action, upload))
          }
        //港口方上传文件和确认吨数
        case `a13FinishedUpload` =>
          entity(as[HarborUploadContract]) { upload =>
            complete(submitA13(party_class, user_id, instance_id, action, upload))
          }
        //贸易方审核
        case `a15traderAudit` =>
          entity(as[TraderAudit]) { audit =>
            complete(submitA15(party_class, user_id, instance_id, action, audit))
          }
        //贸易方给出建议金额
        case `a16traderRecommendAmount` =>
          entity(as[TraderRecommendAmount]) { recommend =>
            complete(submitA16(party_class, user_id, instance_id, action, recommend))
          }
        //资金方审核
        case `a17fundProviderAudit` =>
          entity(as[FundProviderAudit]) { fundAudit =>
            complete(submitA17(party_class, user_id, instance_id, action, fundAudit))
          }
        //资金方财务审核
        case `a18fundProviderAccountantAudit` =>
          entity(as[FundProviderAccountantAudit]) { accountantAduit =>
            complete(submitA18(party_class, user_id, instance_id, action, accountantAduit))
          }
        //融资方确认还款
        case `a19SecondReturnMoney` =>
          entity(as[FinancerToTrader]) { repayment =>
            complete(submitA19(party_class, user_id, instance_id, action, repayment))
        }
        //贸易商通知港口放货
        case `a20noticeHarborRelease` =>
          entity(as[TraffickerNoticePortReleaseGoods]) { release =>
            complete(submitA20(party_class, user_id, instance_id, action, release))
          }
        //港口放货
        case `a21harborRelease` =>
          entity(as[PortReleaseGoods]) { release =>
            complete(submitA21(party_class, user_id, instance_id, action, release))
          }
        //贸易商选择是否完成
        case `a22traderAuditIfComplete` =>
          entity(as[TraffickerAuditIfCompletePayment]) { completePayment =>
            complete(submitA22(party_class, user_id, instance_id, action, completePayment))
          }
        //贸易商确认回款给资金方
        case `a23ReturnMoney` =>
          entity(as[TraffickerConfirmPayToFundProvider]) { confirm =>
            complete(submitA23(party_class, user_id, instance_id, action, confirm))
          }
        //贸易商财务确认回款给资金方
        case `a24AccountantReturnMoney` =>
          entity(as[TraffickerFinancePayToFundProvider]) { confirm =>
            complete(submitA24(party_class, user_id, instance_id, action, confirm))
          }

        case _ => throw BusinessException("不支持的任务类型")
      }

    }
  }


  import com.yimei.cflow.api.util.PointUtil._

  def test = get {
    pathPrefix("fortest" / Segment / Segment / Segment) {
      (flowId, pointName, value) =>

        println(s"!!!!$flowId @@@@ $pointName *** $value")

        val point = Map(pointName -> value.wrap())
        val hijackEntity = HijackEntity(updatePoints = point, trigger = true, decision = None)
        request[HijackEntity, State](path = "api/internal/flow/admin/hijack", pathVariables = Array(flowId), model = Some(hijackEntity), method = "put")
        complete("success")
    }
  }

  /**
    * 流程详情
    *
    * @return
    */
  def flowDetail = get {
    //pathPrefix("financeorders" / Segment / Segment / Segment / Segment) {(user_id, party_class, instance_id, flowId) =>
    (path("financeorders" / Segment) & myRequiredSession) { (flowId, session) =>
      val party_class = session.party
      val user_id = session.userId
      val instance_id = session.instanceId
        //获得流程信息
        complete(cyDataResult(flowId, party_class, instance_id, user_id))
    }
  }


  def route = startFlow ~ submitTask ~ test ~ flowDetail ~ submitFinancerTask ~ financerFlowList ~ flowList ~ financerflowDetail
}


object CangFlowRoute {
  def apply() = new CangFlowRoute

  def route(): Route = CangFlowRoute().route
}
