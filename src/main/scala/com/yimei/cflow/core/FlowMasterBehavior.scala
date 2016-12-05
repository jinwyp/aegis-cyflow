package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.core.Flow.{Command, CommandCreateFlow, CommandRunFlow}
import com.yimei.cflow.integration.ServicableBehavior

/**
  * Created by hary on 16/12/4.
  */

trait FlowMasterBehavior extends Actor with ActorLogging with ServicableBehavior {



  def serving: Receive = {

    // 创建, 并运行流程
    case command @ CommandCreateFlow(flowId, userId) =>
      create(flowId, getModules(), userId) forward  CommandRunFlow

    // 这里是没有uid的, 就是recovery回来的
    case command: Command =>
      log.info(s"get command $command and forward to child!!!!")
      val child = context.child(command.flowId).fold(create(command.flowId, getModules()))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  /**
    * 创建
 *
    * @param flowId      流程id
    * @param modules     依赖的模块服务
    * @param userId      用户Id, 默认是None
    * @param parties     相关方如: 港口 -> 赫萝
    * @return
    */
  def create(flowId: String,
             modules: Map[String, ActorRef],
             userId: String = "",
             parties: Map[String, String] = Map()): ActorRef = {
    // log.info(s"创建流程:($flowId, $modules, $userId, $parties")
    context.actorOf(flowProp(flowId, modules, userId, parties), flowId)
  }

  /**
    * 如何获取Actor的Prop
 *
    * @param flowId    流程id
    * @param modules   依赖的模块
    * @param userId    用户id,  默认为None
    * @param parties   相关方
    * @return
    */
  def flowProp(flowId: String,
               modules: Map[String, ActorRef],
               userId: String,
               parties: Map[String, String]): Props


  /**
    * 获取相关模块
    */
  def getModules(): Map[String, ActorRef]
}
