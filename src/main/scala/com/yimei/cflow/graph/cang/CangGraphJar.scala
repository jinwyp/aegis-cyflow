package com.yimei.cflow.graph.cang

import com.yimei.cflow.api.models.flow.{Arrow, State}
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException

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
      case "1" => Seq(ArrowSuccess)
      case "0" => Seq(FundProviderDisapprove)
      case _   => throw BusinessException("资金方审核提交数据有误")
    }
  }
}
