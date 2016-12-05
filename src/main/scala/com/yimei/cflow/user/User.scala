package com.yimei.cflow.user

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.core.Flow.{CommandQuery, CommandShutdown, DataPoint}
import com.yimei.cflow.user.UserMaster.GetUserData

/**
  * Created by hary on 16/12/2.
  */
object User {

  // 命令
  trait Command {
    def userId: String
  }

  case class CommandStartUser(userId: String, hierarchyInfo: Option[HierarchyInfo] = None)



  case class CommandTaskSubmit(userId: String, taskId: String, points: Map[String, DataPoint]) extends Command

  // 用户提交数据点
  case class CommandShutDown(userId: String) extends Command

  // shutdown用户
  case class CommandMobileCome(userId: String, mobile: ActorRef) extends Command

  // 手机登录成功
  case class CommandDesktopCome(userId: String, desktop: ActorRef) extends Command

  // 添加参与方
  case class CommandAddParties(userId: String, parties: Map[String, Array[String]]) extends Command

  // 电脑登录成功

  // 事件
  trait Event

  case class HierarchyInfoUpdated(hierarchyInfo: HierarchyInfo) extends Event
  case class PartiesAdded(parties: Map[String, Array[String]]) extends Event

  //
  case class TaskEnqueue(taskId: String, task: GetUserData) extends Event

  // 将采集任务保存
  case class TaskDequeue(taskId: String) extends Event

  // 将采集任务删除

  // 用户的session状态
  case class State(hierarchyInfo: Option[HierarchyInfo], tasks: Map[String, GetUserData])

  // 用于对于流程需要处理的消息


  // 人在组织中的位置
  case class HierarchyInfo(superior: Option[String], subordinates: Option[List[String]])

}

import com.yimei.cflow.user.User._

class User(
            userId: String,
            hierarchyInfo: Option[HierarchyInfo],
            modules: Map[String, ActorRef],
            passivateTimeout: Long) extends PersistentActor with ActorLogging {

  import concurrent.duration._

  override def persistenceId = userId

  var state: State = State(hierarchyInfo, Map[String, GetUserData]()) // 用户的状态不断累积!!!!!!!!

  // 多终端在线
  var mobile: ActorRef = null
  var desktop: ActorRef = null

  // 更新用户状态
  def updateState(ev: Event) = {
    ev match {
      case TaskDequeue(taskId) => state = state.copy(tasks = state.tasks - taskId)
      case TaskEnqueue(taskId, task) => state = state.copy(tasks = state.tasks + (taskId -> task))
      case HierarchyInfoUpdated(hinfo) => state = state.copy(hierarchyInfo = Some(hinfo))
    }
    log.info(s"持久化${ev}成功")
  }

  // 超时
  context.setReceiveTimeout(passivateTimeout seconds)

  // 恢复
  def receiveRecover = {
    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)
    case SnapshotOffer(_, snapshot: State) =>
      log.info(s"recover with snapshot: $snapshot")
      state = snapshot
    case RecoveryCompleted =>
      log.info(s"recover completed")
  }

  // 生成任务id
  def uuid() = UUID.randomUUID().toString;

  //
  def receiveCommand = {

    // 启动用户时, 需要更新用户的组织机构关系
    case CommandStartUser(userId, Some(h)) =>
      persist(HierarchyInfoUpdated(h)) { event =>
        updateState(event)
      }

    // 添加参与方
    case CommandAddParties(_, parties) =>
      persist(PartiesAdded(parties)) { event =>
        updateState(event)
      }

    // 采集数据请求
    case command: GetUserData =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid;  // 生成任务id, 将任务保存
      persist(TaskEnqueue(taskId, command)) { event =>
        updateState(event)
        // 如果用mobile在线, 给mobile推送采集任务!!!!!!!!!!!!!!!!!!!!
      }

    // 收到用户提交的采集数据
    case command@CommandTaskSubmit(userId, taskId, points) =>
      log.info(s"收到采集提交: $command")
      val task = state.tasks(taskId)
      persist(TaskDequeue(taskId)) { event =>
        updateState(event)
        modules(task.flowName) ! 1
      }

    // 用户查询
    case command: CommandQuery =>
      log.info(s"收到用户查询: $command")
      sender() ! state

    // 手机登录成功
    case command: CommandMobileCome =>

    // 电脑登录成功
    case command: CommandDesktopCome =>

    // 关闭用户
    case shutdown: CommandShutdown =>
      log.info("收到CommandShutdown")
      context.stop(self)

    // 收到超时
    case ReceiveTimeout =>
      log.info(s"${persistenceId}超时, 开始钝化!!!!")
      context.stop(self)
  }

  override def unhandled(message: Any): Unit = {
    log.error(s"收到未处理消息 $message")
    super.unhandled(message)
  }
}
