package com.yimei.cflow.user

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandPoints, DataPoint}
import com.yimei.cflow.user.User.HierarchyInfo
import com.yimei.cflow.user.UserMaster.GetUserData

/**
  * Created by hary on 16/12/2.
  */
object User {

  ////////////////////////////////////////////////////
  // 命令
  ////////////////////////////////////////////////////
  trait Command {
    def guid: String
  }

  // 0. 创建用户 for UserMaster
  case class CommandCreateUser(guid: String, hierarchyInfo: Option[HierarchyInfo] = None) extends Command

  // 1. 用户提交任务
  case class CommandTaskSubmit(guid: String, taskId: String, points: Map[String, DataPoint]) extends Command

  // 2. shutdown用户
  case class CommandShutDown(guid: String) extends Command

  // 3. 手机登录成功
  case class CommandMobileCome(guid: String, mobile: ActorRef) extends Command

  // 4. 电脑登录
  case class CommandDesktopCome(guid: String, desktop: ActorRef) extends Command

  // 5. 查询用户信息
  case class CommandQueryUser(guid: String) extends Command

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
  case class State(userId: String, userType: String, hierarchyInfo: Option[HierarchyInfo], tasks: Map[String, GetUserData])

  // 人在组织中的位置
  case class HierarchyInfo(superior: Option[String], subordinates: Option[List[String]])


}

class User(guid: String, hierarchyInfo: Option[HierarchyInfo], modules: Map[String, ActorRef]) extends AbstractUser with ActorLogging {

  import User._

  // 用户id与用户类型
  val regex = "(\\w+)-(.*)".r
  val (userId, userType) = guid match {
    case regex(uid, gid) => (uid, gid)
  }

  var state: State = State(userId, userType, hierarchyInfo, Map[String, GetUserData]()) // 用户的状态不断累积!!!!!!!!

  // 生成任务id
  def uuid() = UUID.randomUUID().toString;


  override def receive: Receive = commonBehavior orElse serving

  def serving: Receive = {

    // 采集数据请求
    case command: GetUserData =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid; // 生成任务id, 将任务保存
      updateState(TaskEnqueue(taskId, command))
    // todo 如果用mobile在线, 给mobile推送采集任务!!!!!!!!!!!!!!!!!!!!

    // 收到用户提交的采集数据
    case command@CommandTaskSubmit(guid, taskId, points) =>
      log.info(s"收到采集提交: $command")
      val task = state.tasks(taskId)
      updateState(TaskDequeue(taskId))
      modules(module_flow) ! CommandPoints(task.flowId, points)
  }

  override def unhandled(message: Any): Unit = {
    log.error(s"收到未处理消息 $message")
    super.unhandled(message)
  }
}


