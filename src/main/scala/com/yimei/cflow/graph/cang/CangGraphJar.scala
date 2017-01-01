package com.yimei.cflow.graph.cang

import akka.actor.ActorLogging
import akka.camel.Consumer
import com.yimei.cflow.api.models.auto.CommandAutoTask
import com.yimei.cflow.api.models.flow.{Arrow, State}
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException

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
    Seq(ArrowSuccess)
  }


  //自动任务-------------------------
  //资金方自动付款
  def fundProviderPayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {
    //todo 向数据库中插入一条记录
    Future{Map(fundProviderPaying -> "yes")}
  }


  def traderPayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {
    //todo 向数据库中插入一条记录
    Future{Map(traderPaying -> "yes")}
  }

  def financerPayingTask(cmd: CommandAutoTask): Future[Map[String, String]] = {
    //todo 向数据库中插入一条记录
    Future{Map(financerPaying -> "yes")}
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
