package com.yimei.cflow.user

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow._
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.user.User.{CommandStartUser, HierarchyInfo}
import com.yimei.cflow.user.UserMaster.{CommandStart, GetUserData}


object UserMaster extends Core {

  // 采集用户数据
  case class GetUserData(flowName: String, flowId: String, userId: String, taskName: String)

  case class CommandStart(userId: String)

  /**
    *
    * @param taskName         调用那个用户任务
    * @param state
    * @param flowName
    * @param userMaster
    * @param refetchIfExists
    */
  def ufetch(taskName: String, state: State, flowName: String, userMaster: ActorRef, refetchIfExists: Boolean = false) = {
    val points = taskPointMap(taskName)
    if (refetchIfExists || points.filter(!state.points.contains(_)).length > 0) {
      userMaster ! GetUserData(flowName, state.flowId, state.userId, taskName)
    }
  }

  def props() = Props(new UserMaster)
}

trait UserMasterBehavior extends Actor with ServicableBehavior with ActorLogging {
  override def serving: Receive = {

    // 必须要被先调用, 用来创建用户!!!!!!!!
    case CommandStart(userId) =>
      val hierarchyInfo = HierarchyInfo(None, None)   // todo 王琦把这个改为从数据库读取用户的关系, 通过future来创建
      context.child(userId).fold(create(userId, Some(hierarchyInfo)))(identity)

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

      // 转发消息
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
class UserMaster extends ModuleMaster(module_user, List(module_ying, module_cang)) with UserMasterBehavior {
  override def getModules(): Map[String, ActorRef] = modules
}



