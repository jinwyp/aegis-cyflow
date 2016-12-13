package com.yimei.cflow.group

object Group {

  // Command
  trait Command {
    def ggid: String
  }

  case class CommandCreateGroup(ggid: String) extends Command

  case class CommandGroupTask(flowId: String, ggid: String, taskName: String) extends Command

  case class CommandClaimTask(ggid: String, taskId: String, userId: String) extends Command

  case class CommandQueryGroup(ggid: String) extends Command

  // Event
  trait Event {
    def taskId: String
  }

  case class TaskEnqueue(taskId: String, task: CommandGroupTask) extends Event

  case class TaskDequeue(taskId: String) extends Event

  // State:  group -> taskId -> groupTask
  case class State(gid: String, userType: String, tasks: Map[String, CommandGroupTask])

}





