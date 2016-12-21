package com.yimei.cflow.util

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.graph.ying.YingConfig._
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow.{CreateFlowSuccess, _}
import com.yimei.cflow.core.Flow.Graph
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandCreateUser, CommandTaskSubmit}
import com.yimei.cflow.user.User.CommandUserTask

import scala.concurrent.duration._

case class QueryTest(flowId: String, userId: String)

/**
  * Created by hary on 16/12/4.
  */
class QueryActor(daemon: ActorRef) extends Actor with ActorLogging {

  implicit val ec = context.dispatcher;
  implicit val timeout = Timeout(5 seconds)

  def uuid() = UUID.randomUUID().toString

  var k1: Cancellable = null
  var k2: Cancellable = null

  def receive = {
    case test@QueryTest(flowId, guid) =>

      // 创建用户
      val f1 = daemon ? CommandCreateUser(guid)

      f1 onSuccess {
        case userState =>
          log.info(s"创建用户成功 user state = ${userState}")
          // 发起用户查询
          log.info(s"定期发起用户查询${guid}")
          k1 = context.system.scheduler.schedule(
            1 seconds,
            5 seconds,
            daemon,
            User.CommandQueryUser(guid)
          )
      }

      f1 onFailure {
        case ex => log.info(s"用户创建失败, ex = ${ex}")
      }


      // 先创建流程
      val cmd = CommandCreateFlow("ying", guid)
      log.info(s"发起创建流程${cmd}")
      val f2 = daemon ? cmd
      f2 onSuccess {
        case CreateFlowSuccess(flowId) =>
          // 1> 启动流程
          daemon ! CommandRunFlow(flowId)

          // 2> 发起定时查询
          log.info(s"定期发起流程查询${flowId}")
          k2 = context.system.scheduler.schedule(
            1 seconds,
            13 seconds,
            daemon,
            Flow.CommandFlowGraph(flowId)
          )
      }

    case json @ Graph(_, _, Some(state), _, _, _) =>
      log.info(s"flow[${state.flowId}] = ${state.edges}")
      if ( state.edges.size == 0) {
        k1.cancel()
        k2.cancel()
        log.info("测试结束")
      }

    case state: User.State =>
      log.info(s"收到消息 = $state")
      // 处理用户任务
      state.tasks.foreach { entry =>
        processTask(entry._1, entry._2)
      }
  }

  def processTask(taskId: String, task: CommandUserTask) = {
    log.info(s"处理用户任务: ${taskId}")
    val points = taskPointMap(task.taskName).map { pname =>
        (pname -> DataPoint("50", Some("userdata"), Some(task.guid), uuid, new Date().getTime))    // uuid为采集id
    }.toMap

    daemon ! CommandTaskSubmit(task.guid, taskId, points) // 提交任务处理给daemon
  }
}
