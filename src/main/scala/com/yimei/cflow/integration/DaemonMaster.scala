package com.yimei.cflow.integration

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandCreateFlow, CommandQueryFlow}
import com.yimei.cflow.core.{PersistentEngineMaster, Flow, FlowGraph}
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.user.User.{CommandStartUser, CommandTaskSubmit}
import com.yimei.cflow.user.{User, UserMaster}

// 模块注册于协商
object DaemonMaster {

  case class RegisterModule(name: String, actor: ActorRef)

  case class GiveMeModule(name: String)

  case class UnderIdentify()

  // 测试用
  case class CreateUser(userId: String, commandCreateUser: CommandStartUser)

  case class QueryUser(userId: String, commandQuery: User.CommandQuery)

  // 仓押, 应收, 数据, 用户 4大模块
  def moduleProps(name: String): Props = {
    name match {
      case `module_engine` => PersistentEngineMaster.props(module_engine, Array(module_user, module_data))
      case `module_data` => DataMaster.props()
      case `module_user` => UserMaster.props()
    }
  }

  def props(names: Array[String]) = Props(new DaemonMaster(names))

}


/**
  * Created by hary on 16/12/3.
  */
class DaemonMaster(names: Array[String]) extends Actor with ActorLogging {

  import DaemonMaster._

  // start all modules
  var modules = names.map { name =>
    val m = context.actorOf(moduleProps(name), name)
    context.watch(m)
    (name, m)
  }.toMap

  override def receive = {
    // 谁找我要模块, 我就给谁模块
    case GiveMeModule(name) =>
      log.debug(s"收到GiveMeModule(${name}) from [${sender().path}]")
      val org = sender()
      modules.get(name).foreach(org ! RegisterModule(name, _))

    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////
    // 测试创建流程
    case cmd : CommandCreateFlow =>
      log.info(s"收到${cmd}")
      modules.get(module_engine).foreach(_ forward cmd)

    // 测试查询流程
    case cmd : CommandQueryFlow =>
      log.info(s"收到${cmd}")
      modules.get(module_engine).foreach(_ forward cmd)

    case cmd@CommandStartUser(userId, _) =>
      log.info(s"收到CreateUser(${userId}, ${cmd})")
      modules.get(module_user).foreach(_ forward cmd)

    case QueryUser(userId, cmd) =>
      log.info(s"收到QueryUser(${userId}, ${cmd})")
      modules.get(module_user).foreach(_ forward cmd)

    case cmd@CommandTaskSubmit(userId, _, _) =>
      log.info(s"收到${cmd}")
      modules.get(module_user).foreach(_ forward cmd)

    case Terminated(ref) =>
      val (died, rest) = modules.span(entry => entry._2 == ref);
      modules = rest
      died.foreach { entry =>
        log.warning(s"!!!!!!!!!!!!!!!!!!${entry._1} died")
      }
  }
}

