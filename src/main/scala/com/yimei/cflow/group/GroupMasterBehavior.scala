package com.yimei.cflow.group

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.group.Group.{Command, CommandCreateGroup, CommandQueryGroup}
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}

/**
  * Created by hary on 16/12/12.
  */
trait GroupMasterBehavior extends Actor
  with ServicableBehavior
  with DependentModule
  with ActorLogging {

  def create(ggid: String): ActorRef = {
    context.actorOf(props(ggid),ggid)
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

  def props(ggid: String): Props
}
