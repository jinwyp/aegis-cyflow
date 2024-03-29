package com.yimei.cflow.engine.user

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef}
import com.yimei.cflow.api.models.flow.CommandPoints
import com.yimei.cflow.api.models.user._
import com.yimei.cflow.config.GlobalConfig._


/**
  * Created by hary on 16/12/13.
  */
class MemoryUser(guid: String, modules: Map[String, ActorRef]) extends AbstractUser with ActorLogging {

  // 用户id与用户类型
  val regex = "([^!]+)!(.*)".r
  val (userType, userId) = guid match {
    case regex(uid, gid) => (uid, gid)
  }

  var state: State = State(userId, userType, Map[String, CommandUserTask]()) // 用户的状态不断累积!!!!!!!!

  // 生成任务id
  def uuid() = UUID.randomUUID().toString;

  override def receive: Receive = commonBehavior orElse serving

  def serving: Receive = {

    // 采集数据请求
    case command: CommandUserTask =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid; // 生成任务id, 将任务保存
      updateState(TaskEnqueue(taskId, command))
    //sender() ! state
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
