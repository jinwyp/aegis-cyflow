package com.yimei.cflow.core

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

object IdGenerator {

  def props(name: String): Props = Props(new IdGenerator(name))


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
  * Created by hary on 16/12/9.
  */
class IdGenerator(name: String) extends PersistentActor with ActorLogging {

  import IdGenerator._

  override def persistenceId: String = name

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

  override def receiveRecover = {

    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)

    case SnapshotOffer(_, snapshot: State) =>
      state = snapshot
      log.info(s"snapshot recovered")
    case RecoveryCompleted =>
      logState("recovery completed")

  }

  override def receiveCommand: Receive = {
    case CommandGetId(key) =>
      persistAsync(EventIncrease(key)) { event =>
        log.info(s"event $event persited")
        sender()! Id(state.keys(key) + 1)
      }
  }

}

