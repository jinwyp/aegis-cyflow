package com.yimei.cflow


import java.util.{Date, UUID}

import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.CorsSupport
import com.yimei.cflow.user.User.{CommandCreateUser, CommandQueryUser, CommandTaskSubmit, CreateUserSuccess}
import akka.pattern._
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.user.User
import com.yimei.cflow.user.UserMaster.GetUserData

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

/**
  * Created by hary on 16/12/3.
  */
object ServiceTest extends App with ApplicationConfig with CorsSupport {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  // daemon master and
  val names = Array(module_data, module_user, module_flow)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names, true), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")

  // 0> 创建用户
  def userSuccess: Future[CreateUserSuccess] = (proxy ? CommandCreateUser("hary", None)).mapTo[CreateUserSuccess]

  // 1> 创建流程
  def flowSuccess(userId: String) = (proxy ? CommandCreateFlow(flow_ying, userId)).mapTo[CreateFlowSuccess]

  // 2> 查询流程
  def flowQuery(flowId: String) = (proxy ? CommandQueryFlow(flowId)).mapTo[FlowGraphJson]

  // 3> 查询用户
  def userQuery(userId: String) = (proxy ? CommandQueryUser(userId)).mapTo[User.State]


  Thread.sleep(2000)

  val fall = for {
    u <- userSuccess
    f <- flowSuccess(u.userId)
    _ <- (proxy ? CommandRunFlow(f.flowId))
  } yield(u.userId, f.flowId)

  fall onSuccess {
    case (userId, flowId) =>

    // 发起用户查询
    coreSystem.log.info(s"定期发起用户查询${userId}")
    coreSystem.scheduler.schedule(
      1 seconds,
      5 seconds,
      new Runnable {
        override def run(): Unit = {
          userQuery(userId) onSuccess {
            case state: User.State =>
              coreSystem.log.info(s"收到${state}")
              state.tasks.foreach { entry =>
                processTask(entry._1, entry._2)
              }
          }
        }
      }
    )

    // 发起流程查询
    coreSystem.log.info(s"定期发起流程查询${flowId}")
    coreSystem.scheduler.schedule(
      1 seconds,
      5 seconds,
      new Runnable {
        override def run(): Unit = {
          flowQuery(flowId) onSuccess {
            case flowJson => coreSystem.log.info(s"收到${flowJson}")
          }
        }
      }
    )
  }

  fall onFailure {
    case ex => coreSystem.log.info(s"失败: ${ex}")
  }

  def uuid() = UUID.randomUUID().toString

  def processTask(taskId: String, task: GetUserData) = {
    coreSystem.log.info(s"处理用户任务: ${taskId}")
    val points = taskPointMap(task.taskName).map { pname =>
      (pname -> DataPoint(50, "userdata", task.userId, uuid, new Date()))    // uuid为采集id
    }.toMap

    daemon ! CommandTaskSubmit(task.userId, taskId, points) // 提交任务处理给daemon
  }

}
