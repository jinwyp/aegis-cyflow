package com.yimei.cflow.util

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import com.yimei.cflow.ServiceTest._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{Graph, _}
import com.yimei.cflow.core.FlowProtocol
import com.yimei.cflow.integration.ServiceProxy.{coreExecutor => _, coreSystem => _, coreTimeout => _, _}
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandQueryUser, CommandTaskSubmit}
import com.yimei.cflow.user.UserMaster.CommandUserTask

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/7.
  */
object TestUtil extends CoreConfig {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  def test(proxy: ActorRef, testClient: ActorRef, userType: String, userId: String) = {

    Thread.sleep(2000)

    val fall = for {
      u <- userCreate(proxy, userType, userId)
      g <- flowCreate(proxy, userType, userId, flow_ying)
    } yield (userType, userId, g.state.flowId)

    fall onSuccess {
      case (userType, userId, flowId) =>
        testClient ! (userType, userId, flowId)
      // coreSystem.actorOf(Props(new TestActor(proxy, guid, flowId)))
    }

    fall onFailure {
      case ex => coreSystem.log.info(s"失败: ${ex}")
    }

  }
}

class TestClient(proxy: ActorRef) extends Actor with ActorLogging with FlowProtocol {

  //  coreSystem.log.info(s"定期发起用户查询${userId}")
  //  coreSystem.log.info(s"定期发起流程查询${flowId}")
  var schedulers: Map[String, Cancellable] = Map()

  var count = 0

  override def receive: Receive = {

    // 收到查询任务, 给自己发tick
    case (userType: String, userId: String, flowId: String) =>
      val q: Cancellable = context.system.scheduler.schedule(1 seconds, 5 seconds, self, (userType, userId, flowId, 1))
      schedulers = schedulers + (flowId -> q)

    // tick消息
    case (userType: String, userId: String, flowId: String, 1) =>
      proxy ! CommandQueryUser(s"${userType}-${userId}") //
      proxy ! CommandQueryFlow(flowId)

    case state: User.State =>
      state.tasks.foreach { entry =>
        processTask(entry._1, entry._2)
      }

    case g @ Graph(_, state, _) =>
      if (state.decision == FlowSuccess || state.decision == FlowFail) {
        schedulers(state.flowId).cancel()
        schedulers = schedulers - state.flowId
        count = count + 1
        log.info(s"${state.flowId} completed, completed total = ${count}")
        import spray.json._
        log.info(s"final graph is ${g.toJson}")

      }
  }

  def uuid() = UUID.randomUUID().toString

  def processTask(taskId: String, task: CommandUserTask) = {
    coreSystem.log.info(s"处理用户任务: ${taskId}")
    val points = taskPointMap(task.taskName).map { pname =>
      (pname -> DataPoint("50", Some("userdata"), Some(task.guid), uuid, new Date())) // uuid为采集id
    }.toMap

    proxy ! CommandTaskSubmit(task.guid, taskId, points) // 提交任务处理给daemon
  }
}

