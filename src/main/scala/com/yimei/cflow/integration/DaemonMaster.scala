package com.yimei.cflow.integration

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.CommandCreateFlow
import com.yimei.cflow.core.{Flow, FlowMaster, IdGenerator}
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.user.{User, UserMaster}

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
      case `module_flow` => FlowMaster.props(Array(module_user, module_auto), persist)
      case `module_user` => UserMaster.props(Array(module_flow, module_auto), persist)
      case `module_auto` => DataMaster.props(Array(module_user, module_flow))
      case `module_id`   => IdGenerator.props(name)
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
    log.debug(s"开始创建${name} with persist = ${persist}")
    val m = context.actorOf(moduleProps(name, persist), name)
    context.watch(m)
    (name, m)
  }.toMap

  // 注册Graph!!!!!!
  YingGraph

  override def receive = {
    case GiveMeModule(name) =>
      log.debug(s"收到GiveMeModule(${name}) from [${sender().path}]")
      modules.get(name).foreach(sender() ! RegisterModule(name, _))

    case Terminated(ref) =>
      val (died, rest) = modules.span(entry => entry._2 == ref);
      modules = rest
      died.foreach { entry =>
        log.warning(s"!!!!!!!!!!!!!!!!!!${entry._1} died, restarting...")
        val m = context.actorOf(moduleProps(entry._1, persist), entry._1)
        context.watch(m)
        modules = modules + (entry._1 -> m)
      }

    ////////////////////////////////////////////////////////////////////////
    // 测试Flow
    ////////////////////////////////////////////////////////////////////////
    case cmd: CommandCreateFlow =>
      log.debug(s"收到${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    case cmd: Flow.Command =>
      log.debug(s"收到${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    ////////////////////////////////////////////////////////////////////////
    // 测试user
    ////////////////////////////////////////////////////////////////////////
    case cmd: User.Command =>
      log.debug(s"收到$cmd")
      modules.get(module_user).foreach(_ forward cmd)
  }
}

