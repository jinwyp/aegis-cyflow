package com.yimei.cflow.group

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.user.UserMaster.GetUserData


object PersistentGroup {
  def props(userType: String, modules: Map[String, ActorRef]): Props = Props(new PersistentGroup(userType, modules))
}

/**
  * Created by hary on 16/12/10.
  */

class PersistentGroup(userType: String, modules: Map[String, ActorRef]) extends AbstractGroup with PersistentActor with ActorLogging {

  import Group._

  // 恢复
  override def receiveRecover = {
    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)
    case SnapshotOffer(_, snapshot: State) =>
      log.info(s"recover with snapshot: $snapshot")
      state = snapshot
    case RecoveryCompleted =>
      log.info(s"recover completed")
  }

  override def receiveCommand: Receive = {

    case g@GetGroupData(flowId, userType, group, task) =>
      val taskId = UUID.randomUUID().toString
      val ev = TaskEnqueue(userType, group, taskId, g)
      persist(ev) { event =>
        updateState(event)
        log.info(s"event ${event} persisted")
      }

    case CommandClaimTask(userType, group, taskId, userId) =>
      val task = state.tasks(group)(taskId)
      val ev = TaskDequeue(userType, group, taskId)
      persist(ev) { event =>
        log.info(s"event ${event} persisted")
        updateState(event)
        modules("user") ! GetUserData(task.flowId, s"${userType}-${userId}", task.taskName)
      }

  }

  override def persistenceId: String = userType

}
