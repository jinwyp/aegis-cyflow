package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import com.yimei.cflow.api.http.client.AdminClient
import com.yimei.cflow.graph.cang.models.CangFlowModel._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.yimei.cflow.api.http.models.AdminModel.{AdminProtocol, HijackEntity}
import com.yimei.cflow.graph.cang.config.Config
import spray.json._
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.api.models.database.FlowDBModel.FlowInstanceEntity
import com.yimei.cflow.api.models.flow.State
import com.yimei.cflow.api.util.HttpUtil.request
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.services.FlowService._

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/26.
  * 流程相关路由
  */
class CangFlowRoute extends AdminClient
  with AdminProtocol
  with SprayJsonSupport
  with ResultProtocol
  with Config {

  /**
    * 初始化
    *
    * @return
    */
  def startFlow = post {
    pathPrefix("startflow") {
      entity(as[StartFlow]) { startFlow =>
        //该用户是否已经存在。如果不存在要自动添加。 //todo 大磊哥

        val create: Future[Result[FlowInstanceEntity]] = createFlow(rzf, startFlow.basicInfo.applyCompanyId.toString, startFlow.basicInfo.applyUserId.toString, flowType,
          Map(startPoint -> startFlow.toJson.toString,
              orderId    -> startFlow.basicInfo.businessCode,
              traderUserId -> myfUserId,
              traderAccountantUserId -> myfFinanceId)
        ) map { c =>
          Result(Some(c))
        }
        complete(create)
      }
    }
  }

  /**
    * 提交任务
    * @return
    */
  def submitTask = post {
    pathPrefix("financeorders") {
      pathPrefix("action" / Segment/ Segment/ Segment/ Segment) { (action,user_id,party_class,instance_id) =>
        // todo 大磊哥 获取用户信息
//        val user_id = "77777"
//        val party_class = myf
//        val instance_id = "88888888"

        action match {
          //完成选择港口,监管方和资金方
          case `a11SelectHarborAndSupervisor` =>
            entity(as[TraffickerAssignUsers]) { tAssign =>
            complete(submitA11(party_class, user_id, instance_id, tAssign))
          }
          //融资方上传文件或监管方上传文件
          case `a12FinishedUpload` | `a14FinishedUpload` =>
            entity(as[UploadContract]) {  upload =>
              complete(submitA12AndA14(party_class, user_id, instance_id,action,upload))
            }
          //港口方上传文件和确认吨数
          case `a13FinishedUpload` =>
            entity(as[HarborUploadContract]) { upload =>
              complete(submitA13(party_class, user_id, instance_id,action,upload))
            }
          //贸易方审核
          case `a15traderAudit`  =>
            entity(as[TraderAudit]) { audit =>
              complete(submitA15(party_class, user_id, instance_id,action,audit))
            }
          //贸易方给出建议金额
          case `a16traderRecommendAmount` =>
            entity(as[TraderRecommendAmount]) { recommend =>
              complete(submitA16(party_class, user_id, instance_id,action,recommend))
            }
          //资金方审核
          case `a17fundProviderAudit`   =>
            entity(as[FundProviderAudit]) { fundAudit =>
              complete(submitA17(party_class, user_id, instance_id,action,fundAudit))
            }
          //资金方财务审核
          case `a18fundProviderAccountantAudit` =>
            entity(as[FundProviderAccountantAudit]) { accountantAduit =>
              complete(submitA18(party_class, user_id, instance_id,action,accountantAduit))
            }
          //融资方确认还款
          case `a19SecondReturnMoney` =>
            entity(as[FinancerToTrader]){ repayment =>
              complete(submitA19(party_class, user_id, instance_id,action,repayment))
            }
          //贸易商通知港口放货
          case `a20noticeHarborRelease` =>
            entity(as[TraffickerNoticePortReleaseGoods]) { release =>
              complete(submitA20(party_class, user_id, instance_id,action,release))
            }
          //港口放货
          case `a21harborRelease` =>
            entity(as[PortReleaseGoods]) { release =>
              complete(submitA21(party_class, user_id, instance_id,action,release))
            }
           //贸易商选择是否完成
          case `a22traderAuditIfComplete` =>
            entity(as[TraffickerAuditIfCompletePayment]) { completePayment =>
              complete(submitA22(party_class, user_id, instance_id,action,completePayment))
            }
           //贸易商确认回款给资金方
          case `a23ReturnMoney`          =>
            entity(as[TraffickerConfirmPayToFundProvider]) { confirm =>
              complete(submitA23(party_class, user_id, instance_id,action,confirm))
            }
           //贸易商财务确认回款给资金方
          case `a24AccountantReturnMoney` =>
            entity(as[TraffickerFinancePayToFundProvider]) { confirm =>
              complete(submitA24(party_class, user_id, instance_id,action,confirm))
            }

          case _ => throw BusinessException("不支持的任务类型")
        }
      }
    }
  }

  import com.yimei.cflow.api.util.PointUtil._

  def test = get {
    pathPrefix("fortest"/ Segment / Segment / Segment) { (flowId,pointName,value) =>

      println(s"!!!!$flowId @@@@ $pointName *** $value")

      val point = Map(pointName->value.wrap())
      val hijackEntity = HijackEntity(updatePoints = point,trigger=true,decision=None)
      request[HijackEntity,State](path="api/flow/admin/hijack",pathVariables = Array(flowId),model = Some(hijackEntity),method = "put")
      complete("success")
    }
  }

  /**
    * 流程详情
    * @return
    */
  def flowDetail = get {
    pathPrefix("financeorders"/ Segment / Segment / Segment / Segment /Segment) { (action,user_id,party_class,instance_id,flowId) =>
    //获得流程信息
      complete(cyDataCollection(flowId,party_class,instance_id,user_id))
    }
  }


  def route = startFlow ~ submitTask ~ test
}



object CangFlowRoute {
  def apply() = new CangFlowRoute

  def route(): Route = CangFlowRoute().route
}
