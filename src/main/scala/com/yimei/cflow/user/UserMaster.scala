package com.yimei.cflow.user

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.user.User.{CommandStartUser, HierarchyInfo}
import com.yimei.cflow.user.UserMaster.GetUserData
import com.yimei.cflow.config.GlobalConfig._


object UserMaster extends CoreConfig {

  //
  def ufetch(taskName: String, state: State, userMaster: ActorRef, refetchIfExists: Boolean = false) = {
    if ( refetchIfExists ||
      taskPointMap(taskName).filter(!state.points.contains(_)).length > 0
    ) {
      userMaster ! GetUserData(state.flowId, state.userId, taskName)
    }
  }

  // 采集用户数据
  case class GetUserData(flowId: String, userId: String, taskName: String)

  def props() = Props(new UserMaster)
}

trait UserMasterBehavior extends Actor with ServicableBehavior with ActorLogging {
  override def serving: Receive = {

    // 必须要被先调用, 用来创建用户!!!!!!!!
    case cmd @ CommandStartUser(userId, hierarchyInfo) =>
      val child = context.child(userId).fold(create(userId, hierarchyInfo))(identity)
      child forward cmd

      // 收到流程过来的任务
    case command: GetUserData =>
      val child = context.child(command.userId).fold {
        create(command.userId, None)
      }(identity)
      child forward command

      // 其他用户command
    case command: User.Command =>
      val child = context.child(command.userId).fold {
        create(command.userId, None)
      }(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  def getModules(): Map[String, ActorRef]

  private def create(userId: String, hierarchyInfo: Option[HierarchyInfo]) = {
    context.actorOf(Props(new User(userId, hierarchyInfo, getModules(), 40)), userId)
  }
}

/**
  * Created by hary on 16/12/2.
  */
class UserMaster extends ModuleMaster(module_user, Array(module_engine)) with UserMasterBehavior {
  override def getModules(): Map[String, ActorRef] = modules
}



