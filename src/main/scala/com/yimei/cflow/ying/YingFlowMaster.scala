package com.yimei.cflow.ying

import akka.actor.{ActorRef, Props}
import com.yimei.cflow._
import com.yimei.cflow.core.FlowMasterBehavior
import com.yimei.cflow.integration.ModuleMaster

object YingFlowMaster {
  def props(): Props = Props(new YingFlowMaster)
}

/**
  * Created by hary on 16/12/1.
  */
class YingFlowMaster extends ModuleMaster(module_ying, List(module_data, module_user)) with FlowMasterBehavior {

  override def getModules(): Map[String, ActorRef] = modules

  override def flowProp(flowId: String,
                        modules: Map[String, ActorRef],
                        userId: Option[String],
                        parties: Map[String, String]): Props =
    Ying.props(flowId, getModules(), userId, parties)
}


