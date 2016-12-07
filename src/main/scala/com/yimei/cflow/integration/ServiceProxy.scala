package com.yimei.cflow.integration

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.pattern._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandCreateUser, CommandQueryUser, CreateUserSuccess}

import scala.concurrent.Future

/**
  * Created by hary on 16/12/6.
  */

object ServiceProxy extends CoreConfig {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  /**
    *
    * @param daemon  后端服务的daemon
    * @param modules 需要的模块
    * @return
    */
  def props(daemon: ActorRef, modules: Array[String]) = Props(new ServiceProxy(daemon, modules))


  // 0> 创建用户
  def userCreate(proxy: ActorRef): Future[CreateUserSuccess] = (proxy ? CommandCreateUser("hary", None)).mapTo[CreateUserSuccess]

  // 1> 创建流程
  def flowCreate(proxy: ActorRef, userId: String) = (proxy ? CommandCreateFlow(flow_ying, userId)).mapTo[CreateFlowSuccess]

  // 2> 运行流程
  def flowRun(proxy: ActorRef, flowId: String) = (proxy ? CommandRunFlow(flowId)).mapTo[RunFlowSuccess]

  // 3> 查询流程
  def flowQuery(proxy: ActorRef, flowId: String) = (proxy ? CommandQueryFlow(flowId)).mapTo[FlowGraphJson]

  // 4> 查询用户
  def userQuery(proxy: ActorRef, userId: String) = (proxy ? CommandQueryUser(userId)).mapTo[User.State]

}

/**
  * Created by hary on 16/12/6.
  */
class ServiceProxy(daemon: ActorRef, dependOn: Array[String]) extends ModuleMaster("serviceProxy", dependOn, Some(daemon))
  with ServicableBehavior
  with ActorLogging {

  override def serving: Receive = {

    case cmd: Flow.CommandCreateFlow =>
      log.info(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

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

