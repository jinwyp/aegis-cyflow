package com.yimei.cflow.capital

import akka.actor.{Actor, ActorLogging, Props, SupervisorStrategy, Terminated}

/**
  * Created by hary on 16/12/2.
  */
class CapitalSupervisor  extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  def receive = {
    case Capital.StartUser(userId) =>
      context.child(userId).fold(create(userId))(identity)

    case command: Capital.Command =>
      val child = context.child(command.userId).fold(create(command.userId))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  def create(userId: String) = {
    context.actorOf(Props(new Capital(userId, 40)), userId)
  }
}
