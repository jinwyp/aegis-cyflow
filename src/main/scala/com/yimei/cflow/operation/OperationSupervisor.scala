package com.yimei.cflow.operation

import akka.actor.{Actor, ActorLogging, Props, SupervisorStrategy, Terminated}

/**
  * Created by hary on 16/12/2.
  */
class OperationSupervisor  extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  def receive = {
    case Operation.StartUser(userId) =>
      context.child(userId).fold(create(userId))(identity)

    case command: Operation.Command =>
      val child = context.child(command.userId).fold(create(command.userId))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  def create(userId: String) = {
    context.actorOf(Props(new Operation(userId, 40)), userId)
  }
}
