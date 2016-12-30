package com.yimei.cflow.graph.cang

import com.yimei.cflow.api.models.flow.{Arrow, State}
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel._
import spray.json._

/**
  * Created by wangqi on 16/12/26.
  */
object CangGraphJar extends Config {

  val ArrowFail = Arrow("fail", None)
  val ArrowSuccess = Arrow("success", None)

  val TraderDisapprove = Arrow("TraderDisapprove",None)

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
       case "1" => Seq(ArrowSuccess)
       case "0" => Seq(TraderDisapprove)
       case _ => throw BusinessException("贸易商审核提交数据有误")
     }
  }
}
