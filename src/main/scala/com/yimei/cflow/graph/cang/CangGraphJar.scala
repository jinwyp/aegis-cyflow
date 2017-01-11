package com.yimei.cflow.graph.cang

import java.util.concurrent.TimeUnit

import com.yimei.cflow.api.models.auto.CommandAutoTask
import com.yimei.cflow.api.models.flow.{Arrow, State}
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.services.FlowService._

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/26.
  */
object CangGraphJar extends Config {

  val ArrowFail = Arrow("fail", None)
  val ArrowSuccess = Arrow("success", None)

  //贸易方审核不通过
  val TraderDisapprove = Arrow("TraderDisapprove",None)
  //资金方审核不通过
  val FundProviderDisapprove = Arrow("FundProviderDisapprove",None)
  //资金方财务不通过
  val FundProviderAccountantDisapprove = Arrow("FundProviderAccountantDisapprove",None)

  def financingStep11(state: State) = {
    Seq(Arrow(financingStep12,Some(E1)))
  }

  def financingStep12(state: State) = {
      Seq(Arrow(financingStep13,Some(E2)))
  }

  def financingStep13(state: State) = {
    Seq(Arrow(financingStep14,Some(E3)))
  }

  def financingStep14(state: State) = {
     state.points(traderAuditResult).value match {
       case "1" => Seq(Arrow(financingStep15,Some(E4)))
       case "0" => Seq(TraderDisapprove)
       case _ => throw BusinessException("贸易商审核提交数据有误")
     }
  }

  def financingStep15(state: State) = {
    Seq(Arrow(financingStep16,Some(E5)))
  }

  def financingStep16(state: State) = {
    state.points(fundProviderAuditResult).value match {
      case "1" => Seq(Arrow(financingStep17,Some(E6)))
      case "0" => Seq(FundProviderDisapprove)
      case _   => throw BusinessException("资金方审核提交数据有误")
    }
  }

  def financingStep17(state: State) = {
    state.points(fundProviderAccountantAuditResult).value match {
      case "1" => Seq(Arrow(financingStep18,Some(E7)))
      case "0" => Seq(FundProviderAccountantDisapprove)
      case _   => throw BusinessException("资金方财务审核提交数据有误")
    }
  }


  def financingStep18(state: State) = {
    Seq(Arrow(financingStep19,Some(E8)))
  }

  def financingStep19(state: State) = {
    Seq(Arrow(repaymentStep20,Some(E9)))
  }

  def repaymentStep20(state: State) = {
    Seq(Arrow(repaymentStep21,Some(E10)))
  }

  def repaymentStep21(state: State) = {
    Seq(Arrow(repaymentStep22,Some(E11)))
  }

  def repaymentStep22(state: State) = {
    Seq(Arrow(repaymentStep23,Some(E12)))
  }

  def repaymentStep23(state: State) = {
    Seq(Arrow(repaymentStep24,Some(E13)))
  }

  def repaymentStep24(state: State): Seq[Arrow] = {
    state.points(TraderAuditIfCompletePayment).value match {
      case "1" => Seq(Arrow(repaymentStep25,Some(E15)))
      case "0" => Seq(Arrow(financingStep19,Some(E14)))
      case _   => throw BusinessException("贸易方确认回款完成信息有误")
    }
  }

  def repaymentStep25(state: State) = {
    Seq(Arrow(repaymentStep26,Some(E16)))
  }

  def repaymentStep26(state: State) = {
    Seq(Arrow(repaymentStep27,Some(E17)))
  }

  def repaymentStep27(state: State) = {
    Seq(ArrowSuccess)
  }

  //自动任务-------------------------
  //资金方自动付款
  def fundProviderPayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {
    //向数据库中插入一条记录
    insertIntoCangPay(
      cmd.state.points(fundProviderUserId).value,           //src  - fundProviderUser
      cmd.state.points(traderUserId).value,                 //target - traderUser
      BigDecimal(cmd.state.points(recommendAmount).value),
      cmd.state.flowId,
      fundProviderPaySuccess
    ) map { t =>
      Map(fundProviderPaying -> "yes")
    }

  }


  def traderPayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {
    // 向数据库中插入一条记录
    insertIntoCangPay(
      cmd.state.points(traderUserId).value,                 //src  - traderUserId
      cmd.state.guid,                                       //target - 流程所属者 financerId
      BigDecimal(cmd.state.points(recommendAmount).value),
      cmd.state.flowId,
      traderPaySuccess
    ) map { t =>
      Map(traderPaying -> "yes")
    }
  }

  def financerPayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {
    // 向数据库中插入一条记录
    insertIntoCangPay(
      cmd.state.guid,                                       // src -  流程所属者 financerId
      cmd.state.points(traderUserId).value,                 // target  - traderUserId
      BigDecimal(cmd.state.points(repaymentAmount).value),
      cmd.state.flowId,
      financerPaySuccess
    ) map { t =>
      Map(financerPaying -> "yes")
    }
  }

  def traderRepayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {

    val total: BigDecimal = BigDecimal(cmd.state.points(recommendAmount).value) *
      TimeUnit.MICROSECONDS.toDays(cmd.state.points(TraderAccountantConfirm).timestamp - cmd.state.points(fundProviderPaySuccess).timestamp) *
      BigDecimal(cmd.state.points(fundProviderInterestRate).value)/365

    // 向数据库中插入一条记录
    insertIntoCangPay(
      cmd.state.points(traderUserId).value,                  //src - traderUserId
      cmd.state.points(fundProviderUserId).value,                 // target  - traderUserId
      total,
      cmd.state.flowId,
      traderPaySuccess
    ) map { t =>
      Map(traderRepaying -> "yes")
    }
   // Future{Map(traderRepaying -> "yes")}
  }
}


//这个后面要写，现在直接用http代替。
//object ScheduleTask {
//  class CiticSchedule extends Consumer with ActorLogging {
//    def endpointUri = "quartz://schedule-citic?cron=1+0+*+*+*+?"
//
//    def receive = {
//      case msg =>
//        log.info("开始支付")
//    }
//  }
//}
