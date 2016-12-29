package com.yimei.cflow.graph.cang

import com.yimei.cflow.api.models.flow.{Arrow, State}
import com.yimei.cflow.graph.cang.config.Config

/**
  * Created by wangqi on 16/12/26.
  */
object CangGraphJar extends Config {

  val ArrowFail = Arrow("fail", None)
  val ArrowSuccess = Arrow("success", None)

  def financingStep11(state: State) = {
    Seq(Arrow(financingStep12,Some(E1)))
  }

  def financingStep12(state: State) = {
      Seq(Arrow(financingStep13,Some(E2)))
  }

  def financingStep13(state: State) = {
    Seq(ArrowSuccess)
  }
}
