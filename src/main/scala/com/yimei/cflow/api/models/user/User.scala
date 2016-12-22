package com.yimei.cflow.api.models.user

import akka.actor.ActorRef
import com.yimei.cflow.api.models.flow.DataPoint

trait Command {
  def guid: String
}

// 0. 创建用户 for UserMaster
case class CommandCreateUser(guid: String) extends Command

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

// 采集用户数据
case class CommandUserTask(flowId: String, guid: String, taskName: String,flowType:String)

////////////////////////////////////////////////////
// 事件
////////////////////////////////////////////////////
trait Event

// 将采集任务保存
case class TaskEnqueue(taskId: String, task: CommandUserTask) extends Event

// 将采集任务删除
case class TaskDequeue(taskId: String) extends Event

////////////////////////////////////////////////////
// 状态
////////////////////////////////////////////////////
case class State(userId: String, userType: String, tasks: Map[String, CommandUserTask])