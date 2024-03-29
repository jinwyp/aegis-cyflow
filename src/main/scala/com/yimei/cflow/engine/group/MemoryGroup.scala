package com.yimei.cflow.engine.group

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef}
import com.yimei.cflow.config.GlobalConfig.module_user
import com.yimei.cflow.api.models.user.CommandUserTask

/**
  * Created by hary on 16/12/12.
  */

//object MemoryGroup {
//  //def props(userType: String, modules: Map[String, ActorRef]): Props = Props(new PersistentGroup(userType, modules))
//}

class MemoryGroup(ggid:String,modules:Map[String,ActorRef]) extends AbstractGroup with ActorLogging {
  import com.yimei.cflow.api.models.group._

  // 用户id与用户类型
  val regex = "([^!]+)!(.*)".r
  val (userType, gid) = ggid match {
    case regex(uid, gid) => (uid, gid)
  }

  override var state: State = State(userType, gid, Map[String,CommandGroupTask]()) // group的状态不断累积!!!!!!!!

  // 生成任务id
  def uuid() = UUID.randomUUID().toString

  override def receive: Receive = commonBehavior orElse serving


  def serving: Receive = {

    // 采集数据请求
    case command: CommandGroupTask =>
      log.info(s"收到采集任务: $command")
      val taskId = uuid; // 生成任务id, 将任务保存
      updateState(TaskEnqueue(taskId, command))
    // todo 如果用mobile在线, 给mobile推送采集任务!!!!!!!!!!!!!!!!!!!!

    // 收到用户claim请求
    case command@CommandClaimTask(ggid: String, taskId: String, userId: String) =>
      log.info(s"claim的请求: $command")
      val task = state.tasks(taskId)
      updateState(TaskDequeue(taskId))
      modules(module_user) ! CommandUserTask(task.flowId,s"${userType}!${userId}",task.taskName,task.flowType)
      sender() ! state
  }

  override def unhandled(message: Any): Unit = {
    log.error(s"收到未处理消息! {} from {}",message,sender())
    super.unhandled(message)
  }
}
