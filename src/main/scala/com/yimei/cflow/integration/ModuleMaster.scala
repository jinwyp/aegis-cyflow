package com.yimei.cflow.integration

import akka.actor.{Actor, ActorLogging, ActorRef, ReceiveTimeout, SupervisorStrategy, Terminated}
import com.yimei.cflow._
import com.yimei.cflow.integration.DaemonMaster.{GiveMeModule, RegisterModule, UnderIdentify}

import concurrent.duration._

/**
  * Created by hary on 16/12/3.
  */

abstract class ModuleMaster(moduleName: String, dependOn: List[String])
  extends Actor
    with ServicableBehavior
    with ActorLogging {

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  // 向关联的模块
  var modules: Map[String, ActorRef] = Map(moduleName -> self)

  // 请求父亲告知其他模块
  dependOn.foreach { name =>
    log.info(s"${moduleName} 请求获取${context.parent.path} ${name}")
    context.parent ! GiveMeModule(name)
  }

  // 进入identifing阶段
  context.setReceiveTimeout(20 millis)

  override def receive = identify

  def identify: Receive = {

    // 拿到模块
    case RegisterModule(name, ref) =>
      modules = modules + (name -> ref)
      log.info(s"get module $name")
      context.watch(ref)

      // 如果所有模块都拿到, 就进入服务态
      if (check) {
        log.info(s"${moduleName} is servicable now")
        context.become(serving)
        context.setReceiveTimeout(Duration.Undefined)
      } else {
        context.setReceiveTimeout(10 millis)
      }

    case Terminated(ref) =>
      log.error(s"${ref.path.name} died")
      sender() ! UnderIdentify
      context.setReceiveTimeout(20 millis)

      // 重新向parent要模块
      modules.find(entry => entry._2 == ref).foreach(t =>
        context.parent ! GiveMeModule(t._1)
      )

    // 没有收到, 看还有那些模块没有拿到, 就重新请求parent
    case ReceiveTimeout =>
      if (!modules.contains(module_user)) {
        context.parent ! GiveMeModule(module_user)
      }
      if (!modules.contains(module_data)) {
        context.parent ! GiveMeModule(module_data)
      }
      context.setReceiveTimeout(20 millis)

    // identifying阶段, 不能处理消息
    case msg =>
      log.error(s"${moduleName} are not prepared")
      sender() ! UnderIdentify
  }

  // 检查是否所有的模块启动好
  def check() = dependOn.find(!modules.contains(_)) match {
    case Some(_) => false
    case None => true
  }

}

trait ServicableBehavior {
  def serving: Actor.Receive
}


