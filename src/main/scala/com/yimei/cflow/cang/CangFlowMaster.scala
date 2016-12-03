package com.yimei.cflow.cang

import akka.actor.{ActorRef, Props}
import com.yimei.cflow._
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.FlowMasterBehavior
import com.yimei.cflow.integration.ModuleMaster


object CangFlowMaster extends Core {
  def props() = Props(new CangFlowMaster)
}

/**
  * Created by hary on 16/12/1.
  */
class CangFlowMaster extends ModuleMaster(module_cang, List(module_data)) with FlowMasterBehavior {
  override def flowProp(flowId: String, modules: Map[String, ActorRef]): Props = Cang.props(flowId, getModules())
  override def getModules(): Map[String, ActorRef] = modules
}
