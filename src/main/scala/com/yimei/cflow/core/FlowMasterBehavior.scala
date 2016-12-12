package com.yimei.cflow.core

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.core.Flow.{Command, CommandCreateFlow, CommandRunFlow}
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.IdGenerator._
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.duration._


/**
  * Created by hary on 16/12/4.
  */

trait FlowMasterBehavior extends Actor
  with ActorLogging
  with ServicableBehavior
  with DependentModule {

  /**
    *
    * @return
    */
  def serving: Receive = {

    // create and run flow
    case command@CommandCreateFlow(flowType, guid, parties) =>

      // use IdGenerator to get persistenceId
      // todo check it
      if( false ) {
        implicit val ec = context.system.dispatcher
        implicit val timeout = Timeout( 3 seconds)
        val fpid = (modules(module_id) ? CommandGetId("flow")).mapTo[Id]
        for (pid <- fpid) {
          val flowId = s"${flowType}-${guid}-${pid}" // 创建flowId
          val child = create(flowId, parties)
          child forward CommandRunFlow(flowId)
        }
      }

      // use UUID to generate persistenceId
      val flowId = s"${flowType}-${guid}-${UUID.randomUUID().toString}" // 创建flowId
      val child = create(flowId, parties)
      child forward CommandRunFlow(flowId)

    // other command
    case command: Command =>
      log.debug(s"get command $command and forward to child!!!!")
      val child = context.child(command.flowId).fold(create(command.flowId, Map()))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  /**
    * 创建
    *
    * @param flowId
    * @param parties related parties take in the flow process
    * @return
    */
  def create(flowId: String, parties: Map[String, String] = Map()): ActorRef = {
    // log.info(s"创建流程:($flowId, $modules, $userId, $parties")
    context.actorOf(flowProp(flowId, parties), flowId)
  }

  /**
    *
    *
    * @param flowId  流程id
    * @param parties 相关方
    * @return
    */
  def flowProp(flowId: String,
               parties: Map[String, String]): Props

}
