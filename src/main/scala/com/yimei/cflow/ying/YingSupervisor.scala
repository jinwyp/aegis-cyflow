package com.yimei.cflow.ying

import akka.actor.SupervisorStrategy
import com.yimei.cflow.core.Supervisor

/**
  * Created by hary on 16/12/1.
  */
class YingSupervisor extends Supervisor {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  override def flowProp(flowId: String) = Ying.props(flowId)
}



