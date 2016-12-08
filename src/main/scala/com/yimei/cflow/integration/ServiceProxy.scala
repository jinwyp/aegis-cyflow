package com.yimei.cflow.integration

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.pattern._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandCreateUser, CommandQueryUser, CommandTaskSubmit, HierarchyInfo}

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

  // 管理员更新数据点
  def flowUpdatePoints(proxy: ActorRef, flowId: String, updatePoint: Map[String, Int]): Future[Graph] =
    (proxy ? CommandUpdatePoints(flowId, updatePoint)).mapTo[Graph]

  def flowUpdateParties(proxy: ActorRef, flowId: String, parties: Map[String, String]): Future[Graph] =
  (proxy ? CommandUpdateParties(flowId, parties)).mapTo[Graph]

  // 0> 创建用户
  def userCreate(proxy: ActorRef, userId: String, hierarchyInfo: Option[HierarchyInfo] = None): Future[User.State] =
    (proxy ? CommandCreateUser(userId, hierarchyInfo)).mapTo[User.State]

  // 1> 创建流程 - 自动运行
  def flowCreate(proxy: ActorRef, userId: String, flowType: String) =
    (proxy ? CommandCreateFlow(flowType, userId)).mapTo[Graph]

  // 2> 查询流程
  def flowQuery(proxy: ActorRef, flowId: String) =
    (proxy ? CommandQueryFlow(flowId)).mapTo[Graph]

  // 3> 查询用户
  def userQuery(proxy: ActorRef, userId: String) =
    (proxy ? CommandQueryUser(userId)).mapTo[User.State]

  // 4> 用户提交任务
  def userSubmit(proxy: ActorRef, userId: String, taskId: String, points: Map[String, DataPoint]) =
    (proxy ? CommandTaskSubmit(userId,taskId, points)).mapTo[User.State]

}

/**
  * Created by hary on 16/12/6.
  */
class ServiceProxy(daemon: ActorRef, dependOn: Array[String]) extends ModuleMaster(module_service, dependOn, Some(daemon))
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
    case cmd: DataMaster.GetAutoData =>
      log.debug(s"收到 ${cmd}")
      modules.get(module_flow).foreach(_ forward cmd)

  }
}

