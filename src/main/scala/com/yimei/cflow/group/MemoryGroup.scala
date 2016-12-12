package com.yimei.cflow.group

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.group.Group.{CommandClaimTask, GetGroupData, TaskDequeue, TaskEnqueue}
import com.yimei.cflow.user.UserMaster.GetUserData

/**
  * Created by hary on 16/12/12.
  */

object MemoryGroup {
  def props(userType: String, modules: Map[String, ActorRef]): Props = Props(new PersistentGroup(userType, modules))
}

class MemoryGroup(userType: String, modules: Map[String, ActorRef])
  extends AbstractGroup
    with Actor
    with ActorLogging {

  override def receive: Receive = commonBehavior orElse serving

  def serving: Receive = {
    case g@GetGroupData(flowId, userType, group, task) =>
      val taskId = UUID.randomUUID().toString
      val ev = TaskEnqueue(userType, group, taskId, g)
      updateState(ev)
      log.info(s"event ${ev} persisted")

    case CommandClaimTask(userType, group, taskId, userId) =>
      val task = state.tasks(group)(taskId)
      val ev = TaskDequeue(userType, group, taskId)
      log.info(s"event ${ev} persisted")
      updateState(ev)
      modules("user") ! GetUserData(task.flowId, s"${userType}-${userId}", task.taskName)
  }
}
