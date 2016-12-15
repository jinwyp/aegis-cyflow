package com.yimei.cflow.group

import akka.actor.{ActorRef, Props, Terminated}
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.group.Group.{Command, CommandCreateGroup, CommandGroupTask, CommandQueryGroup}
import com.yimei.cflow.core.FlowRegistry._

object GroupMaster {

  def gfetch(flowType:String, taskName: String, state: State, groupMaster: ActorRef, ggid:String, refetchIfExists: Boolean = false) = {
    if (refetchIfExists ||
      userTask(flowType)(taskName).filter(!state.points.contains(_)).length > 0
    ) {
      println(s"ufetch with ${state.guid}, ${state}")
      groupMaster ! CommandGroupTask(flowType, state.flowId, ggid, taskName)
    }
  }

  def props(dependOn: Array[String],persist: Boolean = true): Props = Props(new GroupMaster(dependOn,persist))
}

/**
  * Created by hary on 16/12/12.
  */
class GroupMaster(dependOn: Array[String], persist: Boolean = true)
  extends ModuleMaster(module_group, dependOn)
  with ServicableBehavior {

  def create(ggid: String): ActorRef = {
    val p = persist match {
      case true  =>  {
        log.info(s"创建persistent group")
        Props(new PersistentGroup(ggid, modules, 20))
      }
      case false => {
        log.info(s"创建non-persistent group")
        Props(new MemoryGroup(ggid, modules))
      }
    }

    context.actorOf(p,ggid)
  }

  override def serving: Receive = {
    case cmd@CommandCreateGroup(ggid) =>
      log.info(s"GroupMaster 收到消息${cmd}")
      val child = context.child(ggid).fold(create(ggid))(identity)
      child forward CommandQueryGroup(ggid)

    case command: Command =>
      val child = context.child(command.ggid).fold(create(command.ggid))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

}
