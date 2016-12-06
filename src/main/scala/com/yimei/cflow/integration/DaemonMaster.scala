package com.yimei.cflow.integration

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandCreateFlow, CommandQueryFlow}
import com.yimei.cflow.core.FlowMaster
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.user.User.{CommandQueryUser, CommandStartUser, CommandTaskSubmit}
import com.yimei.cflow.user.UserMaster

// 模块注册于协商
object DaemonMaster {

  case class RegisterModule(name: String, actor: ActorRef)

  case class GiveMeModule(name: String)

  case class UnderIdentify()

  /**
    * 采用持久化流程还是非持久化流程
    *
    * @param name
    * @param persist
    * @return
    */
  def moduleProps(name: String, persist: Boolean): Props = {
    name match {
      case `module_flow` => FlowMaster.props(module_flow, Array(module_user, module_data), persist)
      case `module_user` => UserMaster.props(persist)
      case `module_data` => DataMaster.props()
    }
  }

  def props(names: Array[String], persist: Boolean = true) = Props(new DaemonMaster(names, persist))

}

/**
  *
  * @param names
  * @param persist 是否为持久化流程
  */
class DaemonMaster(names: Array[String], persist: Boolean = true) extends Actor with ActorLogging {

  import DaemonMaster._

  var modules = names.map { name =>
    val m = context.actorOf(moduleProps(name, persist), name)
    context.watch(m)
    (name, m)
  }.toMap

  override def receive = {
    case GiveMeModule(name) =>
      log.debug(s"收到GiveMeModule(${name}) from [${sender().path}]")
      modules.get(name).foreach(sender() ! RegisterModule(name, _))

    ////////////////////////////////////////////////////////////////////////
    // 测试用
    ////////////////////////////////////////////////////////////////////////
    case cmd: CommandCreateFlow =>
      log.info(s"收到${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 测试查询流程
    case cmd: CommandQueryFlow =>
      log.info(s"收到${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    case cmd@CommandStartUser(userId, _) =>
      log.info(s"收到CreateUser(${userId}, ${cmd})")
      modules.get(module_user).foreach(_ forward cmd)

    case cmd : CommandQueryUser =>
      log.info(s"收到${cmd}")
      modules.get(module_user).foreach(_ forward cmd)

    case cmd@CommandTaskSubmit(userId, _, _) =>
      log.info(s"收到${cmd}")
      modules.get(module_user).foreach(_ forward cmd)

    case Terminated(ref) =>
      val (died, rest) = modules.span(entry => entry._2 == ref);
      modules = rest
      died.foreach { entry =>
        log.warning(s"!!!!!!!!!!!!!!!!!!${entry._1} died, restarting...")
        val m = context.actorOf(moduleProps(entry._1, persist), entry._1)
        context.watch(m)
        modules = modules + (entry._1 -> m)
      }
  }
}

