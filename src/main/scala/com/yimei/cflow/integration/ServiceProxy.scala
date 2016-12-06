package com.yimei.cflow.integration

import akka.actor.{ActorLogging, ActorRef, Props}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.user.User

/**
  * Created by hary on 16/12/6.
  */

object ServiceProxy {
  /**
    *
    * @param daemon  后端服务的daemon
    * @param modules 需要的模块
    * @return
    */
  def props(daemon: ActorRef, modules: Array[String]) = Props(new ServiceProxy(daemon, modules))
}

/**
  * Created by hary on 16/12/6.
  */
class ServiceProxy(daemon: ActorRef, dependOn: Array[String]) extends ModuleMaster("serviceProxy", dependOn)
  with ServicableBehavior
  with ActorLogging {

  override def serving: Receive = {
    // 用户模块交互
    case cmd: User.Command =>
      log.info(s"收到 ${cmd}")
      modules.get(module_user).foreach(_ forward cmd)

    // 流程模块交互
    case cmd: Flow.Command =>
      log.info(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 数据模块交互
    case cmd: DataMaster.GetAutoData =>
      log.info(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

  }
}

