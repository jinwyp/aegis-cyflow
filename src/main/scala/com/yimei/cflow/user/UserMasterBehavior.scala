package com.yimei.cflow.user

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}
import com.yimei.cflow.user.User._
import com.yimei.cflow.user.UserMaster.GetUserData

/**
  * Created by hary on 16/12/6.
  */
trait UserMasterBehavior extends Actor
  with ServicableBehavior
  with DependentModule
  with ActorLogging {
  override def serving: Receive = {

    case cmd@CommandCreateUser(guid, hierarchyInfo) =>
      log.info(s"UserMaster 收到消息${cmd}")
      val child = context.child(guid).fold(create(guid, hierarchyInfo))(identity)
      child forward CommandQueryUser(guid)

    // 收到流程过来的任务
    case command: GetUserData =>
      val child = context.child(command.guid).fold {
        create(command.guid, None)
      }(identity)
      child forward command

    // 其他用户command
    case command: User.Command =>
      val child = context.child(command.guid).fold {
        create(command.guid, None)
      }(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  private def create(guid: String, hierarchyInfo: Option[HierarchyInfo]) = {
    context.actorOf(props(guid, hierarchyInfo), guid)
  }

  def props(userId: String, hierarchyInfo: Option[HierarchyInfo]): Props

}
