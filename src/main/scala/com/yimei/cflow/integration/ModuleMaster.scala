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

  // 相关联的模块
  var modules: Map[String, ActorRef] = Map(moduleName -> self)   // 自己也在里面

  // 请求父亲告知其他模块
  dependOn.foreach { name =>
    log.info(s"${moduleName} 请求获取 ${name}")
    context.parent ! GiveMeModule(name)
  }

  // 进入identifing阶段
  context.setReceiveTimeout(20 millis)

  override def receive = identify

  def initHook(): Unit = {
    log.info("initHook is void")
  }

  def identify: Receive = {

    // 拿到模块
    case RegisterModule(name, ref) =>
      modules = modules + (name -> ref)
      log.info(s"get module $name")
      context.watch(ref)

      // 如果所有模块都拿到, 就进入服务态
      if (check) {
        log.info(s"${moduleName} is servicable now")
        initHook()
        context.become(serving)
        context.setReceiveTimeout(Duration.Undefined)
      } else {
        context.setReceiveTimeout(10 millis)
      }

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

  override def unhandled(message: Any): Unit = {
    message match {
        // 依赖死亡
      case Terminated(ref) =>
        log.error(s"$moduleName restart because of ${ref.path.name} died")
        context.setReceiveTimeout(20 millis)
        context.become(identify)  // 重新变为identify阶段
        context.children.foreach(context.stop(_))  // 停止所有的child

        // 重新获取依赖的模块
        modules.find(entry => entry._2 == ref).foreach(t =>
          context.parent ! GiveMeModule(t._1)
        )

      case _ =>
        log.error(s"无法处理消息$message")
        super.unhandled(message)
    }
  }

}

trait ServicableBehavior {
  def serving: Actor.Receive
}


