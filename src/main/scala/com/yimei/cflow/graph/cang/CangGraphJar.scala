package com.yimei.cflow.graph.cang

import com.yimei.cflow.api.annotation.VertexProperty
import com.yimei.cflow.api.models.flow.{Arrow, State}

/**
  * Created by wangqi on 16/12/26.
  */
object CangGraphJar {

  val ArrowFail = Arrow("fail", None)
  val ArrowSuccess = Arrow("success", None)

  def financingStep12(state: State) = {
      Seq(ArrowSuccess)
  }

}
