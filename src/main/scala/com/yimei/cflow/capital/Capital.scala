package com.yimei.cflow.capital

import akka.actor.{ActorLogging, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.core.Flow.CommandShutdown

/**
  * Created by hary on 16/12/2.
  */

object Capital {
  case class CapitalSession()

  case class StartUser(userId: String)

  // Command
  trait Command { def userId: String }
  case class CommandX(userId: String) extends Command
  case class CommandShutDown(userId: String) extends Command

  trait Event

  case class State()
}

class Capital(userId: String, passivateTimeout: Long) extends PersistentActor with ActorLogging {

  import Capital._

  import concurrent.duration._

  override def persistenceId = userId

  var state: State = State()

  // 更新状态
  def updateState(ev: Event) = {
  }

  context.setReceiveTimeout(passivateTimeout seconds)

  // 恢复
  def receiveRecover = {
    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)
    case SnapshotOffer(_, snapshot: State) =>
      log.info(s"recover with snapshot: $snapshot")
      state = snapshot
    case RecoveryCompleted =>
      log.info(s"recover completed")
  }

  def receiveCommand = {

    case shutdown: CommandShutdown =>
      log.info("收到CommandShutdown")
      context.stop(self)

    // 收到超时
    case ReceiveTimeout =>
      log.info(s"${persistenceId}超时, 开始钝化!!!!")
      context.stop(self)

    case _ =>
  }
}
