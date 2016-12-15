package com.yimei.cflow.util

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import com.yimei.cflow.ServiceTest.coreSystem
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.FlowProtocol
import com.yimei.cflow.graph.ying.YingConfig.{flow_ying, taskPointMap}
import com.yimei.cflow.integration.ServiceProxy.{flowCreate, userCreate}
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandQueryUser, CommandTaskSubmit, CommandUserTask}
import scala.concurrent.duration._

/**
  * Created by wangqi on 16/12/15.
  */
object TestPart extends CoreConfig {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  def test(proxy: ActorRef, testClient: ActorRef, userType: String, userId: String, userType2:String, userId2:String) = {

    Thread.sleep(2000)

    val fall = for {
      u <- userCreate(proxy, userType, userId)
      pu <- userCreate(proxy,userType2,userId2)
      g <- flowCreate(proxy, userType, userId, flow_ying)
    } yield (userType, userId, g.state.flowId,userType2,userId2)

    fall onSuccess {
      case (userType, userId, flowId,userType2, userId2) =>
        testClient ! (userType, userId, flowId, userType2, userId2)
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
    case (userType: String, userId: String, flowId: String, userType2:String, userId2:String) =>
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
      (pname -> DataPoint("50", Some("userdata"), Some(task.guid), uuid, new Date().getTime)) // uuid为采集id
    }.toMap

    proxy ! CommandTaskSubmit(task.guid, taskId, points) // 提交任务处理给daemon
  }
}
