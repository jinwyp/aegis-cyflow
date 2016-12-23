package com.yimei.cflow.api.services

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.pattern._
import com.yimei.cflow.api.models.flow.{CommandCreateFlow, CommandFlowGraph, CommandFlowState, CommandHijack, CommandUpdatePoints, DataPoint, Graph, Command => FlowCommand, State => FlowState}
import com.yimei.cflow.api.models.group.{Command => GroupCommand, State => GroupState, _}
import com.yimei.cflow.api.models.id.{CommandGetId, CommandQueryId, Id, Command => IdGeneratorCommand, State => IdGeneratorState}
import com.yimei.cflow.api.models.user.{CommandCreateUser, CommandQueryUser, CommandTaskSubmit, Command => UserCommand, State => UserState}
import com.yimei.cflow.auto.AutoMaster
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._

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
  def flowCreate(proxy: ActorRef, userType: String, userId: String, flowType: String,init:Map[String,String] = Map()) =
    (proxy ? CommandCreateFlow(flowType, s"${userType}!${userId}",init)).mapTo[FlowState]

  def flowGraph(proxy: ActorRef, flowId: String) =
    (proxy ? CommandFlowGraph(flowId)).mapTo[Graph]

  def flowState(proxy: ActorRef, flowId: String) =
    (proxy ? CommandFlowState(flowId)).mapTo[FlowState]

  def flowUpdatePoints(proxy: ActorRef, flowId: String, updatePoint: Map[String, String], trigger: Boolean): Future[FlowState] =
    (proxy ? CommandUpdatePoints(flowId, updatePoint, false)).mapTo[FlowState]   // todo

  def flowHijack(proxy: ActorRef, flowId: String, updatePoints: Map[String, DataPoint], decision: Option[String], trigger: Boolean): Future[FlowState] =
    (proxy ? CommandHijack(flowId, updatePoints, decision, trigger)).mapTo[FlowState]   // todo
  // 1> 创建用户
  // 2> 查询用户
  // 3> 用户提交任务
  def userCreate(proxy: ActorRef, userType: String, userId: String): Future[UserState] =
    (proxy ? CommandCreateUser(s"${userType}!${userId}")).mapTo[UserState]

  def userQuery(proxy: ActorRef, userType: String, userId: String) =
    (proxy ? CommandQueryUser(s"${userType}!${userId}")).mapTo[UserState]

  def userSubmit(proxy: ActorRef, userType: String, userId: String, taskId: String, points: Map[String, DataPoint]) =
    (proxy ? CommandTaskSubmit(s"${userType}!${userId}", taskId, points)).mapTo[UserState]

  // Id模块
  // 1> get id
  // 2> query IdGenerator state
  def idGet(proxy: ActorRef, key: String, buffer: Int = 1) = (proxy ? CommandGetId(key, buffer)).mapTo[Id]

  def idState(proxy: ActorRef) = (proxy ? CommandQueryId).mapTo[IdGeneratorState]

  // Group模块  todo 王琦
  // 1> 创建group
  // 2> 查询group
  // 3> claim task
  // 4> 发送group task 测试用
  def groupCreate(proxy: ActorRef, userType: String, gid: String): Future[GroupState] =
    (proxy ? CommandCreateGroup(s"${userType}!${gid}")).mapTo[GroupState]

  def groupQuery(proxy: ActorRef, userType: String, gid: String) =
    (proxy ? CommandQueryGroup(s"${userType}!${gid}")).mapTo[GroupState]

  def groupClaim(proxy: ActorRef, userType: String, gid: String, userId: String, taskId: String): Future[GroupState] =
    (proxy ? CommandClaimTask(s"${userType}!${gid}", taskId, userId)).mapTo[GroupState]

  def groupTask(proxy: ActorRef, userType: String, gid: String, flowId: String, taskName: String, flowType: String): Unit =
    proxy ! CommandGroupTask(flowType, flowId, s"${userType}!${gid}", taskName)

  def autoTask(proxy:ActorRef, state:FlowState, flowType:String, actorName:String) =
    proxy ! CommandAutoTask(state,flowType,actorName)

}

/**
  * Created by hary on 16/12/6.
  */
class ServiceProxy(daemon: ActorRef, dependOn: Array[String]) extends ModuleMaster(module_proxy, dependOn, Some(daemon))
  with ServicableBehavior
  with ActorLogging {

  override def serving: Receive = {

    // 创建流程
    case cmd: CommandCreateFlow =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 用户模块交互
    case cmd: UserCommand =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_user).foreach(_ forward cmd)

    // 流程模块交互
    case cmd: FlowCommand =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

    // 数据模块交互
    case cmd: AutoMaster.CommandAutoTask =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_auto).foreach(_ forward cmd)

    // 与用户组模块交互
    case cmd: GroupCommand =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_group).foreach(_ forward cmd)

    // 与Id模块交互
    case cmd: IdGeneratorCommand =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_id).foreach(_ forward cmd)
  }
}

