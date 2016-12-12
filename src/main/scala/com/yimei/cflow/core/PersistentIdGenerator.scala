package com.yimei.cflow.core

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}

object PersistentIdGenerator {
  def props(name: String): Props = Props(new PersistentIdGenerator(name))
}

/**
  * Created by hary on 16/12/9.
  */
class PersistentIdGenerator(name: String) extends AbstractIdGenerator with PersistentActor with ActorLogging {

  import IdGenerator._

  override def persistenceId: String = name

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

  override def receiveCommand = commonBehavior orElse serving

  def serving: Receive = {
    case CommandGetId(key) =>
      persistAsync(EventIncrease(key)) { event =>
        updateState(event)
        log.info(s"event $event persisted")
        sender()! Id(state.keys(key) + 1)
      }
  }
}

