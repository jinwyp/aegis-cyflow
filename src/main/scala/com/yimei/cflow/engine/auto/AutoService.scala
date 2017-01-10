package com.yimei.cflow.engine.auto

import akka.actor.ActorRef
import com.yimei.cflow.api.models.auto.CommandAutoTask
import com.yimei.cflow.api.models.flow.{State  => FlowState}

/**
  * Created by hary on 17/1/6.
  */
trait AutoService {
  def proxy: ActorRef

  def autoTask(state: FlowState, flowType: String, actorName: String) =
    proxy ! CommandAutoTask(state, flowType, actorName)
}
