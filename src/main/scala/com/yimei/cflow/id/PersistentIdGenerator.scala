package com.yimei.cflow.id

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, RecoveryCompleted, SaveSnapshotSuccess, SnapshotOffer}
import com.yimei.cflow.id.models._

object PersistentIdGenerator {
  def props(name: String, nodeId: Int = 0): Props = Props(new PersistentIdGenerator(name, nodeId))
}

/**
  * Created by hary on 16/12/9.
  */
class PersistentIdGenerator(name: String, nodeId: Int) extends AbstractIdGenerator with PersistentActor with ActorLogging {

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

  //  var cnt = 0

  def serving: Receive = {
    case CommandGetId(key, buffer) =>
      persist(EventIncrease(key, buffer)) { event =>

        val old = updateState(event)
        log.info(s"event $event persisted")
        sender() ! Id(old + 1)

      }
  }
}

