package com.yimei.cflow.group

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.group.Group.Command
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}
import com.yimei.cflow.user.User.HierarchyInfo

/**
  * Created by hary on 16/12/12.
  */
trait GroupMasterBehavior extends Actor
  with ServicableBehavior
  with DependentModule
  with ActorLogging {

  def create(userType: String): ActorRef = {
    context.actorOf(props(userType))
  }

  override def serving: Receive = {
    case cmd : Command =>
      val child = context.child(cmd.userType).fold(create(cmd.userType,???))(identity)
  }

  def props(userType: String): Props
}
