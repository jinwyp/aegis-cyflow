package com.yimei.cflow.user

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.integration.ModuleMaster


object UserMaster extends CoreConfig {

  def ufetch(taskName: String, state: State, userMaster: ActorRef, refetchIfExists: Boolean = false) = {
    if (refetchIfExists ||
      taskPointMap(taskName).filter(!state.points.contains(_)).length > 0
    ) {
      userMaster ! GetUserData(state.flowId, state.userId, taskName)
    }
  }

  // 采集用户数据
  case class GetUserData(flowId: String, userId: String, taskName: String)

  def props(persist: Boolean = true) = Props(new UserMaster(persist))
}

/**
  * Created by hary on 16/12/2.
  */
class UserMaster(persist: Boolean = true) extends ModuleMaster(module_user, Array(module_flow)) with UserMasterBehavior {

}



