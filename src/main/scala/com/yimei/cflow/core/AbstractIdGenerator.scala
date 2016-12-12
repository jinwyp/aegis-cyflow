package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging, Props}

object IdGenerator {
  // Command
  trait Command

  case class CommandGetId(key: String) extends Command
  case object CommandQueryId extends Command

  case class Id(id: BigInt)  // 返回的id

  // Event
  trait Event

  case class EventIncrease(key: String) extends Event

  // State
  case class State(keys: Map[String, Long])

  // create IdGenerator Props
  def props(name: String, persist: Boolean = true) = persist match {
    case true => Props(new PersistentIdGenerator(name))
    case false => Props(new MemoryIdGenerator(name))
  }
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
        val nextId = if( state.keys.contains(key)) {
          state.keys(key) + 1L
        } else {
          0L
        }
        state = state.copy(keys = state.keys + (key -> nextId))
    }
  }

  def logState(mark: String) = {
    log.info(s"<${mark}> cur state: $state")
  }

  def commonBehavior: Receive = {
    case CommandQueryId => sender() ! state.keys
  }

}
