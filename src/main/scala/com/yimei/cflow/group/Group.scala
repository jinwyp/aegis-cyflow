package com.yimei.cflow.group

import akka.actor.{ActorRef, Props}

object Group {

  // Command
  trait Command { def userType: String }

  case class GetGroupData(flowId: String, userType: String, group: String, taskName: String)

  case class CommandClaimTask(userType: String, group: String, taskId: String, userId: String)

  case class CommandQueryGroup(userType: String, group: String)

  // Event
  trait Event {
    def taskId: String
  }

  case class TaskEnqueue(userType: String, group: String, taskId: String, task: GetGroupData) extends Event

  case class TaskDequeue(userType: String, group: String, taskId: String) extends Event

  // State:  group -> taskId -> groupTask
  case class State(tasks: Map[String, Map[String, GetGroupData]])

  //
  def props(userType: String, modules: Map[String, ActorRef], persist: Boolean = true): Props =
    persist match {
      case true => Props(new MemoryGroup(userType, modules))
      case false => Props(new PersistentGroup(userType, modules))
    }

}



