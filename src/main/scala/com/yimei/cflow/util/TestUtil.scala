package com.yimei.cflow.util

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef, Cancellable}
import com.yimei.cflow.ServiceTest._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.{Graph, _}
import com.yimei.cflow.core.FlowProtocol
import com.yimei.cflow.graph.ying.YingConfig._
import com.yimei.cflow.group.Group
import com.yimei.cflow.group.Group._
import com.yimei.cflow.integration.ServiceProxy.{coreExecutor => _, coreSystem => _, coreTimeout => _, _}
import com.yimei.cflow.user.User
import com.yimei.cflow.user.User.{CommandQueryUser, CommandTaskSubmit, CommandUserTask}

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/7.
  */
object TestUtil extends CoreConfig {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  def test(flowType: String, proxy: ActorRef, testClient: ActorRef, userType: String, userId: String,
           pUserType: String, pUserId: String, pGroupType: String, pGroupId: String) = {

    val fall = for {
      u <- userCreate(proxy, userType, userId)
      pu <- userCreate(proxy, pUserType, pUserId)
      pg <- groupCreate(proxy, pGroupType, pGroupId)
      fs <- flowCreate(proxy, userType, userId, flowType)
    } yield (userType, userId, fs.flowId, pUserType, pUserId, pGroupType, pGroupId)

    fall onSuccess {
      case (userType, userId, flowId, pUserType, pUserId, pGroupType, pGroupId) =>
        testClient !(userType, userId, flowId, pUserType, pUserId, pGroupType, pGroupId)
      // coreSystem.actorOf(Props(new TestActor(proxy, guid, flowId)))
    }

    fall onFailure {
      case ex => coreSystem.log.info(s"失败: ${ex}")
    }

  }
}

object TempValue {
  var values: Map[String, (String, String, String)] = Map()

}

class TestClient(proxy: ActorRef) extends Actor
  with ActorLogging
  with FlowProtocol {

  //  coreSystem.log.info(s"定期发起用户查询${userId}")
  //  coreSystem.log.info(s"定期发起流程查询${flowId}")
  var schedulers: Map[String, Cancellable] = Map()

  var count = 0

  //  var gUserId: String = null
  //  var gUserType:String = null
  //
  //  var gId:String = null
  //  var gType:String = null

  import TempValue._

  override def receive: Receive = {

    // 收到查询任务, 给自己发tick
    case (userType: String, userId: String, flowId: String, pUserType: String, pUserId: String, pGroupType: String, pGroupId: String) =>
      //      gUserId = pUserId
      //      gUserType = pUserType
      //      gId = pGroupId
      //      gType = pGroupType
      values = values + (flowId ->(pUserType, pUserId, pGroupId))
      val q: Cancellable = context.system.scheduler.schedule(1 seconds, 5 seconds, self, (userType, userId, flowId,
        pUserType, pUserId, pGroupType, pGroupId, 1))
      schedulers = schedulers + (flowId -> q)

    // tick消息
    case (userType: String, userId: String, flowId: String, pUserType: String, pUserId: String, pGroupType: String, pGroupId: String, 1) =>
      proxy ! CommandQueryUser(s"${userType}-${userId}") //
      proxy ! CommandQueryUser(s"${pUserType}-${pUserId}")
      proxy ! CommandQueryGroup(s"${pGroupType}-${pGroupId}")
      proxy ! CommandFlowGraph(flowId)

    // 收到用户状态, 就自动处理用户任务
    case state: User.State =>
      log.info("!!!!state:{}", state)
      state.tasks.foreach { (entry: (String, CommandUserTask)) =>
        processTask(entry._1, entry._2)
      }

    // 收到组状态, 对每个组的任务进行claim
    case state: Group.State =>
      log.info("!!!groupstate:{}", state)
      state.tasks.foreach(t =>
        proxy ! CommandClaimTask(s"${state.userType}-${state.gid}", t._1, values(t._2.flowId)._2)
      )

    // 收到流程图
    case g@Graph(_, _, st, _, _, _) =>
      st match {
        case Some(state) =>
          if (state.edges.size == 0 ) {
            schedulers(state.flowId).cancel()
            schedulers = schedulers - state.flowId
            count = count + 1
            log.info(s"${state.flowId} completed, completed total = ${count}")
            import spray.json._
            log.info(s"final graph is ${g.toJson}")
          }
      }
  }

  def uuid() = UUID.randomUUID().toString

  def processTask(taskId: String, task: CommandUserTask) = {
    coreSystem.log.info(s"处理用户任务: ${taskId}")
    var points: Map[String, DataPoint] = null
    //设置参与方用户
    if (task.taskName == "TKPU1") {
      points = taskPointMap(task.taskName).points.map { pname =>
        //(pname -> DataPoint("fund-wangqiId", Some("userdata"), Some(task.guid), uuid, new Date().getTime))
        (pname -> DataPoint(values(task.flowId)._1 + "-" + values(task.flowId)._2, Some("userdata"), Some(task.guid), uuid, new Date().getTime))
      }.toMap
    }
    // 设置参与方组
    else if (task.taskName == "TKPG1") {
      points = taskPointMap(task.taskName).points.map { pname =>
        //(pname -> DataPoint("fund-wqGroup", Some("userdata"), Some(task.guid), uuid, new Date().getTime))
        (pname -> DataPoint(values(task.flowId)._1 + "-" + values(task.flowId)._3, Some("userdata"), Some(task.guid), uuid, new Date().getTime))
      }.toMap
    }
    // 其他为用户任务
    else {
      points = taskPointMap(task.taskName).points.map { pname =>
        (pname -> DataPoint("50", Some("userdata"), Some(task.guid), uuid, new Date().getTime)) // uuid为采集id
      }.toMap
    }
    proxy ! CommandTaskSubmit(task.guid, taskId, points) // 提交任务处理给daemon
  }
}

