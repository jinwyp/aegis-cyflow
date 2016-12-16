package com.yimei.cflow.integration

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.pattern._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.{Flow, IdGenerator}
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.auto.AutoMaster
import com.yimei.cflow.core.IdGenerator.{CommandGetId, CommandQueryId, Id}
import com.yimei.cflow.group.Group
import com.yimei.cflow.group.Group.{State, _}
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandCreateUser, CommandQueryUser, CommandTaskSubmit}

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

  // 1> 创建流程 - 自动运行
  // 2> 查询流程
  // 3> 管理员更新数据点
  def flowCreate(proxy: ActorRef, userType: String, userId: String, flowType: String) =
    (proxy ? CommandCreateFlow(flowType, s"${userType}-${userId}")).mapTo[Graph]
  def flowQuery(proxy: ActorRef, flowId: String) =
    (proxy ? CommandFlowGraph(flowId)).mapTo[Graph]
  def flowUpdatePoints(proxy: ActorRef, flowId: String, updatePoint: Map[String, String]): Future[Flow.State] =
    (proxy ? CommandUpdatePoints(flowId, updatePoint)).mapTo[Flow.State]

  // 1> 创建用户
  // 2> 查询用户
  // 3> 用户提交任务
  def userCreate(proxy: ActorRef, userType: String, userId: String): Future[User.State] =
    (proxy ? CommandCreateUser(s"${userType}-${userId}")).mapTo[User.State]
  def userQuery(proxy: ActorRef, userType: String, userId: String) =
    (proxy ? CommandQueryUser(s"${userType}-${userId}")).mapTo[User.State]
  def userSubmit(proxy: ActorRef, userType: String, userId: String, taskId: String, points: Map[String, DataPoint]) =
    (proxy ? CommandTaskSubmit(s"${userType}-${userId}", taskId, points)).mapTo[User.State]

  // Id模块
  // 1> get id
  // 2> query IdGenerator state
  def idGet(proxy: ActorRef, key: String, buffer: Int = 1) = (proxy ? CommandGetId(key, buffer)).mapTo[Id]
  def idState(proxy: ActorRef) = (proxy ? CommandQueryId).mapTo[IdGenerator.State]

  // Group模块  todo 王琦
  // 1> 创建group
  // 2> 查询group
  // 3> claim task
  // 4> 发送group task 测试用
  def groupCreate(proxy: ActorRef, userType: String, gid: String): Future[Group.State] =
    (proxy ? CommandCreateGroup(s"${userType}-${gid}")).mapTo[Group.State]
  def groupQuery(proxy: ActorRef, userType: String, gid: String) =
    (proxy ? CommandQueryGroup(s"${userType}-${gid}")).mapTo[Group.State]
  def groupClaim(proxy: ActorRef, userType: String, gid: String, userId: String, taskId: String): Future[Group.State] =
    (proxy ? CommandClaimTask(s"${userType}-${gid}", taskId, userId)).mapTo[Group.State]
  def groupTask(proxy: ActorRef, userType: String, gid: String, flowId: String, taskName: String,flowType:String): Unit =
    proxy ! CommandGroupTask(flowType,flowId, s"${userType}-${gid}", taskName)

}

/**
  * Created by hary on 16/12/6.
  */
class ServiceProxy(daemon: ActorRef, dependOn: Array[String]) extends ModuleMaster(module_proxy, dependOn, Some(daemon))
  with ServicableBehavior
  with ActorLogging {

  override def serving: Receive = {

    // 创建流程
    case cmd: Flow.CommandCreateFlow =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 用户模块交互
    case cmd: User.Command =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_user).foreach(_ forward cmd)

    // 流程模块交互
    case cmd: Flow.Command =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 数据模块交互
    case cmd: AutoMaster.CommandAutoTask =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 与用户组模块交互
    case cmd: Group.Command =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_group).foreach(_ forward cmd)

    // 与Id模块交互
    case cmd: IdGenerator.Command =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_id).foreach(_ forward cmd)
  }
}

