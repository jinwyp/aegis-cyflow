package com.yimei.cflow.core

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.core.Flow.{Command, CommandCreateFlow, CommandRunFlow, CreateFlowSuccess}
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}


/**
  * Created by hary on 16/12/4.
  */

trait FlowMasterBehavior extends Actor
  with ActorLogging
  with ServicableBehavior
  with DependentModule {

  /**
    * @param flowId
    * @return (graph, userId)
    */
  def getFlowInfo(flowId: String): (FlowGraph, String) = ???

  /**
    *
    * @return
    */
  def serving: Receive = {

    // 创建, 并运行流程
    case command @ CommandCreateFlow(flowType, userId, parties) =>
      val flowId =  s"${flowType}-${userId}-${UUID.randomUUID().toString}"   // 创建flowId
      val child = create(flowId, parties)
      child forward CommandRunFlow(flowId)

    // 这里是没有uid的, 就是recovery回来的
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
    * @param flowId      流程id
    * @param parties     相关方如: 港口 -> 赫萝
    * @return
    */
  def create(flowId: String, parties: Map[String, String] = Map()): ActorRef = {
    // log.info(s"创建流程:($flowId, $modules, $userId, $parties")
    context.actorOf(flowProp(flowId, parties), flowId)
  }

  /**
    * 如何获取Actor的Prop
 *
    * @param flowId    流程id
    * @param parties   相关方
    * @return
    */
  def flowProp(flowId: String,
               parties: Map[String, String]): Props

}
