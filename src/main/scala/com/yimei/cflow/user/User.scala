package com.yimei.cflow.user

import akka.actor.{ActorLogging, ActorRef, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.core.Flow.{CommandQuery, CommandShutdown}

import scala.collection.immutable.Queue

/**
  * Created by hary on 16/12/2.
  */

object User {
  // 启动user
  case class StartUser(userId: String)

  // 命令
  trait Command { def userId: String }
  case class CommandDataPoint(userId: String) extends Command                        // flow派发任务
  case class CommandDataPointSubmit(userId: String) extends Command                  // 用户提交数据
  case class CommandShutDown(userId: String) extends Command                         // shutdown用户
  case class CommandMobileCome(userId: String, mobile: ActorRef) extends Command     // 手机登录成功
  case class CommandDesktopCome(userId: String, desktop: ActorRef) extends Command   // 电脑登录成功

  // 事件
  trait Event
  case class TodoEnqueue(todo: TodoMessage) extends Event
  case class TodoDequeue(rest: Queue[TodoMessage]) extends Event

  // 用户的session状态
  case class State(tasks: Queue[TodoMessage])  // 用于对于流程需要处理的消息

  // 待处理消息
  trait TodoMessage { def flowId: String }
  case class TodoMessageA(flowId: String) extends TodoMessage
  case class TodoMessageB(flowId: String) extends TodoMessage
  case class TodoMessageC(flowId: String) extends TodoMessage

  // 人在组织中的位置
  case class HierarchyInfo(superior: Option[String], subordinates: Option[List[String]])
}

import User._

class User(userId: String, hierarchyInfo: HierarchyInfo, passivateTimeout: Long) extends PersistentActor with ActorLogging {

  import concurrent.duration._

  override def persistenceId = userId

  var state: State = State(Queue[TodoMessage]())   // 用户的状态不断累积!!!!!!!!

  // 多终端在线
  var mobile: ActorRef = null
  var desktop: ActorRef = null

  // 更新用户状态
  def updateState(ev: Event) = ev match {
    case TodoDequeue(rest) => state = state.copy(tasks = rest)
    case TodoEnqueue(todo) => state = state.copy(tasks = state.tasks.enqueue(todo))
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

  // 收到消息
  private def handleTodoMessage(todo: TodoMessage): Unit = {

  }

  def receiveCommand = {
    // 采集数据
    case command: CommandDataPoint =>
      log.info(s"收到采集任务: $command")
      persist(null: Event){ event =>
        // 入队列!!!!!!
      }

    // 采集数据提交
    case command: CommandDataPointSubmit =>
      log.info(s"收到采集提交: $command")
      // 先校验: 因为流程任务必须按顺序完成
      persist(null: Event){ event =>
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
