package com.yimei.cflow.util

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.integration.DaemonMaster.QueryUser
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandStartUser, CommandTaskSubmit, CreateUserSuccess, HierarchyInfo}
import com.yimei.cflow.user.UserMaster.GetUserData

import scala.concurrent.duration._

case class QueryTest(flowName: String, flowId: String, userId: String)

/**
  * Created by hary on 16/12/4.
  */
class QueryActor(daemon: ActorRef) extends Actor with ActorLogging {

  implicit val ec = context.dispatcher;
  implicit val timeout = Timeout(5 seconds)

  def uuid() = UUID.randomUUID().toString

  def receive = {
    case test@QueryTest(flowName, flowId, userId) =>

      // 创建用户
      val f1 = daemon ? CommandStartUser(userId, Some(HierarchyInfo(Some("ceo"), Some(List("s1", "s2")))))

      f1 onSuccess {
        case CreateUserSuccess =>

          // 发起用户查询
          log.info(s"定期发起查询${userId}")
          context.system.scheduler.schedule(
            1 seconds,
            5 seconds,
            daemon,
            QueryUser(userId, User.CommandQuery(userId))
          )
      }


      // 先创建流程
      val cmd = CommandCreateFlow(YingGraph, flowId, userId)
      log.info(s"发起创建流程${cmd}")
      val f2 = daemon ? cmd
      f2 onSuccess {
        case CreateFlowSuccess =>
          //  然后发起查询
          log.info(s"定期发起流程查询${flowName} ${flowId} ${userId}")

          context.system.scheduler.schedule(
            1 seconds,
            13 seconds,
            daemon,
            Flow.CommandQueryFlow(flowId)
          )
      }

    case json: FlowGraphJson =>
      log.info(s"收到消息 = $json")

    case state: User.State =>
      log.info(s"收到消息 = $state")
      // 处理用户任务
      state.tasks.foreach { entry => processTask(entry._1, entry._2)
      }
  }

  def processTask(taskId: String, task: GetUserData) = {
    val point = DataPoint(50, "userdata", "hary", uuid, new Date())
    log.info(s"处理用户任务: ${
      taskId
    }")
    val points = taskPointMap(task.taskName).map {
      pname =>
        (pname -> DataPoint(50, "userdata", "hary", uuid, new Date()))
    }.toMap

    daemon ! CommandTaskSubmit("hary", taskId, points) // 提交任务处理给daemon
  }
}
