package com.yimei.cflow.group

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.config.GlobalConfig.module_user
import com.yimei.cflow.user.User.CommandUserTask

import scala.concurrent.duration._


/**
  * Created by hary on 16/12/10.
  */

class PersistentGroup(ggid: String, modules: Map[String, ActorRef], passivateTimeout: Long) extends AbstractGroup with PersistentActor with ActorLogging {

  import Group._

  println(s"create persistenter group with guid = $ggid")


  // 用户id与用户类型
  val regex = "(\\w+)-(.*)".r
  val (userType, gid) = ggid match {
    case regex(uid, gid) => (uid, gid)
  }

  override def persistenceId = ggid

  override var state: State = State(gid, userType, Map[String, CommandGroupTask]()) // group的状态不断累积!!!!!!!!

  // 超时
  context.setReceiveTimeout(passivateTimeout seconds)


  // 恢复
  override def receiveRecover = {
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
  def uuid() = UUID.randomUUID().toString

  override def receiveCommand: Receive = commonBehavior orElse serving


  def serving: Receive = {

    // 采集数据请求
    case command: CommandGroupTask =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid; // 生成任务id, 将任务保存
      persist(TaskEnqueue(taskId, command)) { event =>
        updateState(event)
      }

    // 收到用户claim请求
    case command@CommandClaimTask(ggid: String, taskId: String, userId: String) =>
      log.info(s"claim的请求: $command")
      val task: CommandGroupTask = state.tasks(taskId)
      persist(TaskDequeue(taskId)) { event =>
        updateState(event)
        modules(module_user) ! CommandUserTask(task.flowId, s"${userType}-${userId}", task.taskName, task.flowType)
        sender() ! state
      }

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
