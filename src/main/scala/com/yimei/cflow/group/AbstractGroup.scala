package com.yimei.cflow.group

import akka.actor.Actor.Receive

/**
  * Created by hary on 16/12/12.
  */
class AbstractGroup {

  import Group._
  import com.softwaremill.quicklens._

  //
  var state: State = State(Map())

  def updateState(event: Event) = {
    event match {
      case TaskEnqueue(_, group, taskId, task) =>
        state =
          if (state.tasks.contains(group)) {
            state.modify(_.tasks.at(group)).setTo(
              state.tasks(group) + (taskId -> task)
            )
          } else {
            state.modify(_.tasks).setTo(Map(group -> Map(taskId -> task)))
          }

      case TaskDequeue(_, group, taskId) =>
        state.modify(_.tasks.at(group)).setTo(state.tasks(group) - taskId)
    }
  }

  def commonBehavior: Receive = {
    case CommandQueryGroup(_, group) => state
  }
}
