package com.yimei.cflow.group

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.integration.ModuleMaster
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.group.Group.CommandGroupTask

object GroupMaster {

  def gfetch(taskName: String, state: State, userMaster: ActorRef, refetchIfExists: Boolean = false) = {
    if (refetchIfExists ||
      taskPointMap(taskName).filter(!state.points.contains(_)).length > 0
    ) {
      println(s"ufetch with ${state.guid}, ${state}")
      userMaster ! CommandGroupTask(state.flowId, state.guid, taskName)
    }
  }

  def props(dependOn: Array[String],persist: Boolean = true): Props = Props(new GroupMaster(dependOn,persist))
}

/**
  * Created by hary on 16/12/12.
  */
class GroupMaster(dependOn: Array[String], persist: Boolean = true)
  extends ModuleMaster(module_group, dependOn)
    with GroupMasterBehavior{

  override def props(ggid: String): Props = {
    persist match {
      case true  =>  {
        log.info(s"创建persistent group")
        Props(new PersistentGroup(ggid, modules, 20))
      }
      case false => {
        log.info(s"创建non-persistent group")
        Props(new MemoryGroup(ggid, modules))
      }
    }
  }

}
