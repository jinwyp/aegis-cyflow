package com.yimei.cflow.api.models.id

import akka.actor.{Actor, ActorLogging, Props}
import com.yimei.cflow.core.{MemoryIdGenerator, PersistentIdGenerator}

// Command
trait Command

case class CommandGetId(key: String, buffer: Int = 1) extends Command
case object CommandQueryId extends Command

case class Id(id: Long)  // 返回的id

// Event
trait Event

case class EventIncrease(key: String, buffer: Int) extends Event

// State
case class State(keys: Map[String, Long])

// create IdGenerator Props
  object IdGenerator {
  def props(name: String, persist: Boolean = true) = persist match {

//  import akka.actor.{Actor, ActorLogging}

    case true => Props(new PersistentIdGenerator(name))
    case false => Props(new MemoryIdGenerator(name))
  }
}


/**
  * Created by hary on 16/12/12.
  */
trait AbstractIdGenerator extends Actor with ActorLogging {

  var state = State(Map())

  def updateState(event: Event): Long = {
    event match {
      case EventIncrease(key, buffer) =>
        val nextId = if( state.keys.contains(key)) {
          state.keys(key) + buffer
        } else {
          0L
        }
        state = state.copy(keys = state.keys + (key -> nextId))
        nextId
    }
  }

  def logState(mark: String) = {
    log.info(s"<${mark}> cur state: $state")
  }

  def commonBehavior: Receive = {
    case CommandQueryId => sender() ! state
  }

}

