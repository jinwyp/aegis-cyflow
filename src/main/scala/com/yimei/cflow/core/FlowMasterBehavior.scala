package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.core.Flow.{Command, CommandQuery, CommandStarting}
import com.yimei.cflow.integration.ServicableBehavior

/**
  * Created by hary on 16/12/4.
  */

trait FlowMasterBehavior extends Actor with ActorLogging with ServicableBehavior {

  def serving: Receive = {

//    case StartFlow(flowId, userId) =>
//      context.child(flowId).fold(create(flowId, getModules(), Some(userId)))(identity)
    case command @ CommandQuery(flowId, uid) =>
      val child = context.child(flowId).fold{
        log.info(s"commandQuery trigger creation with $flowId, $uid")
        create(flowId, getModules(), uid)
      }(identity)
      child ! CommandStarting(uid)
      child forward CommandQuery(flowId)

    // 这里是没有uid的
    case command: Command =>
      val child = context.child(command.flowId).fold(create(command.flowId, getModules()))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  def create(flowId: String,
             modules: Map[String, ActorRef],
             userId: Option[String] = None,
             parties: Map[String, String] = Map()) =
    context.actorOf(flowProp(flowId, modules, userId, parties), flowId)

  def flowProp(flowId: String,
               modules: Map[String, ActorRef],
               userId: Option[String],
               parties: Map[String, String]): Props

  def getModules(): Map[String, ActorRef]
}
