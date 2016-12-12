package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging}

object IdGenerator {
  // Command
  trait Command

  case class CommandGetId(key: String) extends Command

  case class Id(id: BigInt)  // 返回的id

  // Event
  trait Event

  case class EventIncrease(key: String) extends Event

  case class State(keys: Map[String, BigInt])
}

/**
  * Created by hary on 16/12/12.
  */
trait AbstractIdGenerator extends Actor with ActorLogging {

  import IdGenerator._

  var state = State(Map())

  def updateState(event: Event) = {
    event match {
      case EventIncrease(key) =>
        val entry = (key, state.keys(key) + 1)
        state = state.copy(keys = state.keys + entry)
    }
  }

  def logState(mark: String) = {
    log.info(s"<${mark}> cur state: $state")
  }

}
