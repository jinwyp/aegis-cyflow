package com.yimei.cflow.group

import akka.actor.{Actor, ActorLogging}
import akka.actor.Actor.Receive

/**
  * Created by hary on 16/12/12.
  */
abstract class AbstractGroup extends Actor with ActorLogging {

  import Group._

  // 抽象方法
  var state: State

  def updateState(event: Event) = {
    event match {
      case TaskDequeue(taskId) => state = state.copy(tasks = state.tasks - taskId)
      case TaskEnqueue(taskId,task) => state = state.copy(tasks = state.tasks + (taskId -> task))
    }
    log.info(s"${event} persisted, state = ${state}")
  }

  def commonBehavior: Receive = {
    case command:CommandQueryGroup =>
      log.info(s"收到group查询：$command")
      sender() ! state
  }
}
