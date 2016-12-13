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
      println(s"ufetch with ${state.guid}, ${state}")
      userMaster ! CommandUserTask(state.flowId, state.guid, taskName)
    }
  }

  // 采集用户数据
  case class CommandUserTask(flowId: String, guid: String, taskName: String)

  def props(dependOn: Array[String], persist: Boolean = true) = Props(new UserMaster(dependOn, persist))

}

/**
  * Created by hary on 16/12/2.
  */
class UserMaster(dependOn: Array[String], persist: Boolean = true) extends ModuleMaster(module_user, dependOn)
  with UserMasterBehavior {

  override def props(guid: String): Props = {
    persist match {
      case true  =>  {
        log.info(s"创建persistent user")
        Props(new PersistentUser(guid, modules, 20))
      }
      case false => {
        log.info(s"创建non-persistent user")
        Props(new MemoryUser(guid, modules))
      }
    }
  }
}



