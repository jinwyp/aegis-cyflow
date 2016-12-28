package com.yimei.cflow.graph.cang

import com.yimei.cflow.api.annotation.Description
import com.yimei.cflow.api.models.flow.{Arrow, State}

/**
  * Created by wangqi on 16/12/26.
  */
object CangGraphJar {

  val ArrowFail = Arrow("fail", None)
  val ArrowSuccess = Arrow("success", None)

  @Description("等待融资方,港口和监管方上传合同及单据")
  def financingStep12(state: State) = {
      Seq(ArrowSuccess)
  }

}
