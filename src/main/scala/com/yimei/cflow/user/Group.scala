package com.yimei.cflow.user

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.user.UserMaster.GetUserData


object Group {

  // command
  case class GetGroupData(flowId: String, userType: String,  group: String, taskName: String)

  case class ClaimTask(userType: String, group: String, taskId: String, userId: String)

  trait Event {
    def taskId: String
  }

  // 将采集任务保存
  case class TaskEnqueue(userType: String, group: String, taskId: String, task: GetGroupData) extends Event

  // 将采集任务删除
  case class TaskDequeue(userType: String, group: String, taskId: String) extends Event

  ////////////////////////////////////////////////////
  // 状态: userType -> group -> taskId -> task
  ////////////////////////////////////////////////////
  case class State(tasks: Map[String, Map[String, Map[String, GetGroupData]]])


}

/**
  * Created by hary on 16/12/10.
  */
class Group(modules: Map[String, ActorRef]) extends PersistentActor with ActorLogging {

  import Group._
  import com.softwaremill.quicklens._


  var state: State =  State(Map())


  def updateState(event: Event) = {
    event match {
        // 任务入队
      case TaskEnqueue(userType, group, taskId,task) =>
        state = {
          if (state.tasks.contains(userType)) {
            if (state.tasks(userType).contains(group)) {
              state.modify(_.tasks.at(userType).at(group)).setTo(
                state.tasks(userType)(group) + (taskId -> task)
              )
            } else {
              state.modify(_.tasks.at(userType)).setTo(Map(group -> Map(taskId -> task)))
            }
          } else {
            state.copy(tasks = state.tasks + (userType -> Map(group -> Map(taskId -> task))))
          }
        }

        // 任务出队
      case TaskDequeue(userType, group, taskId) =>
        state.modify(_.tasks.at(userType).at(group)).setTo(state.tasks(userType)(group) - taskId)
    }
  }

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

    //
    case g @ GetGroupData(flowId, userType, group, task) =>
      val taskId = UUID.randomUUID().toString
      val ev = TaskEnqueue(userType, group, taskId, g)
      persist(ev) { event =>
        log.info(s"event ${event} persisted")
      }

    // 获取任务, 将任务发送给自己自己
    case ClaimTask(userType, group, taskId, userId) =>
      val task = state.tasks(userType)(group)(taskId)

      // 将任务发送给用户
      val ev = TaskDequeue(userType, group, taskId)
      persist(ev) { event =>
        log.info(s"event ${event} persisted")
        modules("user") ! GetUserData(task.flowId, s"${userType}-${userId}", task.taskName)
      }
  }

  override def persistenceId: String = "GroupMaster"

}

