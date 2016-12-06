package com.yimei.cflow.user

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}
import com.yimei.cflow.user.User.{CommandStartUser, HierarchyInfo}
import com.yimei.cflow.user.UserMaster.GetUserData

/**
  * Created by hary on 16/12/6.
  */
trait UserMasterBehavior extends Actor
  with ServicableBehavior
  with DependentModule
  with ActorLogging {
  override def serving: Receive = {

    // 必须要被先调用, 用来创建用户!!!!!!!!
    case cmd@CommandStartUser(userId, hierarchyInfo) =>
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

  private def create(userId: String, hierarchyInfo: Option[HierarchyInfo]) = {
    context.actorOf(Props(new PersistentUser(userId, hierarchyInfo, modules, 40)), userId)
  }
}
