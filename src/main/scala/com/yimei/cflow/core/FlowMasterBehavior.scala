package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.core.Flow.{Command, CommandCreateFlow, CommandRunFlow}
import com.yimei.cflow.integration.{DependentModule, ServicableBehavior}

/**
  * Created by hary on 16/12/4.
  */

trait FlowMasterBehavior extends Actor with ActorLogging with ServicableBehavior with DependentModule {

  def serving: Receive = {

    // 创建, 并运行流程
    case command @ CommandCreateFlow(graph, flowId, userId) =>
      create(flowId, graph, userId) forward  CommandRunFlow

    // 这里是没有uid的, 就是recovery回来的
    case command: Command =>
      log.debug(s"get command $command and forward to child!!!!")
      val child = context.child(command.flowId).fold(create(command.flowId))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  /**
    * 创建
 *
    * @param flowId      流程id
    * @param userId      用户Id, 默认是None
    * @param parties     相关方如: 港口 -> 赫萝
    * @return
    */
  def create(
             flowId: String,
             graph: FlowGraph = null,
             userId: String = "",
             parties: Map[String, String] = Map()): ActorRef = {
    // log.info(s"创建流程:($flowId, $modules, $userId, $parties")
    context.actorOf(flowProp(graph, flowId, userId, parties), flowId)
  }

  /**
    * 如何获取Actor的Prop
 *
    * @param flowId    流程id
    * @param userId    用户id,  默认为None
    * @param parties   相关方
    * @return
    */
  def flowProp(graph: FlowGraph,
               flowId: String,
               userId: String,
               parties: Map[String, String]): Props

}
