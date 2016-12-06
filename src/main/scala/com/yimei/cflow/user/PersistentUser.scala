package com.yimei.cflow.user

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandPoints, DataPoint}
import com.yimei.cflow.user.User.HierarchyInfo
import com.yimei.cflow.user.UserMaster.GetUserData

import scala.concurrent.duration._


class PersistentUser(userId: String,
                     hierarchyInfo: Option[HierarchyInfo],
                     modules: Map[String, ActorRef],
                     passivateTimeout: Long) extends PersistentActor with ActorLogging {

  import com.yimei.cflow.user.User._

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
      case HierarchyInfoUpdated(hinfo) => state = state.copy(hierarchyInfo = hinfo)
    }
    log.info(s"${ev} persisted")
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
    case cmd@CommandStartUser(userId, hierarchyInfo) =>
      log.info(s"收到用户 ${cmd}")
      persist(HierarchyInfoUpdated(hierarchyInfo)) { event =>
        updateState(event)
        sender() ! CreateUserSuccess
      }

    // 采集数据请求
    case command: GetUserData =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid; // 生成任务id, 将任务保存
      persist(TaskEnqueue(taskId, command)) { event =>
        updateState(event)
        // todo 如果用mobile在线, 给mobile推送采集任务!!!!!!!!!!!!!!!!!!!!
      }

    // 收到用户提交的采集数据
    case command@CommandTaskSubmit(userId, taskId, points: Map[String, DataPoint]) =>
      log.info(s"收到采集提交: $command")
      val task = state.tasks(taskId)
      persist(TaskDequeue(taskId)) { event =>
        updateState(event)
        modules(module_flow) ! CommandPoints(task.flowId, points)
      }

    // 用户查询
    case command: CommandQueryUser =>
      log.info(s"收到用户查询: $command")
      sender() ! state

    // 手机登录成功
    case command: CommandMobileCome =>

    // 电脑登录成功
    case command: CommandDesktopCome =>

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

