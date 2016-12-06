package com.yimei.cflow.user

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, ReceiveTimeout}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandPoints, DataPoint}
import com.yimei.cflow.user.User.HierarchyInfo
import com.yimei.cflow.user.UserMaster.GetUserData

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/2.
  */
object User {

  // 启动用户
  case class CommandStartUser(userId: String, hierarchyInfo: Option[HierarchyInfo] = None)

  case object CreateUserSuccess

  ////////////////////////////////////////////////////
  // 命令
  ////////////////////////////////////////////////////
  trait Command {
    def userId: String
  }

  // 1. 用户提交任务
  case class CommandTaskSubmit(userId: String, taskId: String, points: Map[String, DataPoint]) extends Command

  // 2. shutdown用户
  case class CommandShutDown(userId: String) extends Command

  // 3. 手机登录成功
  case class CommandMobileCome(userId: String, mobile: ActorRef) extends Command

  // 4. 电脑登录
  case class CommandDesktopCome(userId: String, desktop: ActorRef) extends Command

  // 5. 查询用户信息
  case class CommandQueryUser(userId: String) extends Command

  ////////////////////////////////////////////////////
  // 事件
  ////////////////////////////////////////////////////
  trait Event

  //  用户组织信息更新
  case class HierarchyInfoUpdated(hierarchyInfo: Option[HierarchyInfo]) extends Event

  // 将采集任务保存
  case class TaskEnqueue(taskId: String, task: GetUserData) extends Event

  // 将采集任务删除
  case class TaskDequeue(taskId: String) extends Event

  ////////////////////////////////////////////////////
  // 状态
  ////////////////////////////////////////////////////
  case class State(hierarchyInfo: Option[HierarchyInfo], tasks: Map[String, GetUserData])

  // 人在组织中的位置
  case class HierarchyInfo(superior: Option[String], subordinates: Option[List[String]])

}

class User(userId: String,
           hierarchyInfo: Option[HierarchyInfo],
           modules: Map[String, ActorRef],
           passivateTimeout: Long) extends Actor with ActorLogging {

  import com.yimei.cflow.user.User._

  var state: State = State(hierarchyInfo, Map[String, GetUserData]()) // 用户的状态不断累积!!!!!!!!

  // 多终端在线
  var mobile: ActorRef = null
  var desktop: ActorRef = null

  // 更新用户状态
  def updateState(ev: Event) = {
    ev match {
      case TaskDequeue(taskId) => state = state.copy(tasks = state.tasks - taskId)
      case TaskEnqueue(taskId, task) => state = state.copy(tasks = state.tasks + (taskId -> task))
      case HierarchyInfoUpdated(hinfo) => state = state.copy(hierarchyInfo = hinfo)
    }
    log.info(s"${ev} persisted")
  }

  // 超时
  context.setReceiveTimeout(passivateTimeout seconds)

  // 生成任务id
  def uuid() = UUID.randomUUID().toString;

  def receive = {

    // 启动用户时, 需要更新用户的组织机构关系
    case cmd@CommandStartUser(userId, hierarchyInfo) =>
      log.info(s"收到用户 ${cmd}")
      updateState(HierarchyInfoUpdated(hierarchyInfo))
      sender() ! CreateUserSuccess

    // 采集数据请求
    case command: GetUserData =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid; // 生成任务id, 将任务保存
      updateState(TaskEnqueue(taskId, command))
    // todo 如果用mobile在线, 给mobile推送采集任务!!!!!!!!!!!!!!!!!!!!

    // 收到用户提交的采集数据
    case command@CommandTaskSubmit(userId, taskId, points) =>
      log.info(s"收到采集提交: $command")
      val task = state.tasks(taskId)
      updateState(TaskDequeue(taskId))
      modules(module_flow) ! CommandPoints(task.flowId, points)

    // 用户查询
    case command: CommandQueryUser =>
      log.info(s"收到用户查询: $command")
      sender() ! state

    // 手机登录成功
    case command: CommandMobileCome =>

    // 电脑登录成功
    case command: CommandDesktopCome =>
  }

  override def unhandled(message: Any): Unit = {
    log.error(s"收到未处理消息 $message")
    super.unhandled(message)
  }
}


