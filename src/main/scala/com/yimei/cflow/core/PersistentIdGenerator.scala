package com.yimei.cflow.core

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SaveSnapshotSuccess, SnapshotOffer}
import akka.remote.transport.ThrottlerTransportAdapter.Direction.Receive

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

    case SaveSnapshotSuccess =>
  }

  override def receiveCommand = commonBehavior orElse serving

  var cnt = 0

  def serving: Receive = {
    case CommandGetId(key) =>
      persistAsync(EventIncrease(key)) { event =>
        updateState(event)
        log.info(s"event $event persisted")
        sender()! Id(state.keys(key) + 1)
        cnt = cnt + 1
        if ( cnt == 50) {
          log.debug(s"save snapshot of IdGenerator now...")
          saveSnapshot(state)
        }
      }
  }
}

