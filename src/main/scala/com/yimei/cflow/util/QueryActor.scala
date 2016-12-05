package com.yimei.cflow.util

import akka.actor.{Actor, ActorLogging, ActorRef}
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.core.Flow.{CommandCreateFlow, CommandQuery, CreateSuccess, FlowGraphJson}
import com.yimei.cflow.integration.DaemonMaster.{CreateFlow, QueryFlow, QueryTest}

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/4.
  */
class QueryActor(daemon: ActorRef) extends Actor with ActorLogging {

  implicit val ec = context.dispatcher;
  implicit val timeout = Timeout(5 seconds)

  def receive = {
    case test@QueryTest(flowName, flowId, userId) =>
      // 先创建流程
      log.info(s"发起创建流程${flowName} ${flowId} ${userId}")
      val f = daemon ? CreateFlow(flowName, CommandCreateFlow(flowId, userId))

      val kself = self
      f onSuccess {
        case CreateSuccess =>
          //  然后发起查询
          log.info(s"定期发起流程查询${flowName} ${flowId} ${userId}")

          context.system.scheduler.schedule(
            1 seconds,
            13 seconds,
            daemon,
            QueryFlow(flowName, CommandQuery(flowId))
          )
      }

    case FlowGraphJson(json) =>
      context.system.log.info(s"收到消息json = $json")
  }
}
