package com.yimei.cflow.user

import akka.actor.{ActorRef, Props, Terminated}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.user.User.{CommandCreateUser, CommandQueryUser}
import com.yimei.cflow.user.User.CommandUserTask
import com.yimei.cflow.core.FlowRegistry._

object UserMaster extends CoreConfig {

  def ufetch(flowType:String, taskName: String, state: State, userMaster: ActorRef, guid:String ,refetchIfExists: Boolean = false) = {
    if (refetchIfExists ||
      userTask(flowType)(taskName).filter(!state.points.filter(t=>(!t._2.used)).contains(_)).length > 0
    ) {
      // println(s"ufetch with ${taskName}, ${state.guid}, ${state}")
      userMaster ! CommandUserTask(state.flowId, guid, taskName,flowType)
    }
  }



  def props(dependOn: Array[String], persist: Boolean = true) = Props(new UserMaster(dependOn, persist))

}

/**
  * Created by hary on 16/12/2.
  */
class UserMaster(dependOn: Array[String], persist: Boolean = true)
  extends ModuleMaster(module_user, dependOn)
  with ServicableBehavior {

  override def serving: Receive = {

    case cmd@CommandCreateUser(guid) =>
      log.info(s"UserMaster 收到消息${cmd}")
      val child = context.child(guid).fold(create(guid))(identity)
      child forward CommandQueryUser(guid)

    // 收到流程过来的任务
    case command: CommandUserTask =>
      val child = context.child(command.guid).fold {
        create(command.guid)
      }(identity)
      child forward command

    // 其他用户command
    case command: User.Command =>
      val child = context.child(command.guid).fold {
        create(command.guid)
      }(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  private def create(guid: String) = {
    val p = persist match {
      case true  =>  {
        log.info(s"创建persistent user")
        Props(new PersistentUser(guid, modules, 20))
      }
      case false => {
        log.info(s"创建non-persistent user")
        Props(new MemoryUser(guid, modules))
      }
    }
    context.actorOf(p, guid)
  }

}



