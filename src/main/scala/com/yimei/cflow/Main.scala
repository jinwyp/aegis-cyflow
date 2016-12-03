package com.yimei.cflow

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow.CommandQuery
import com.yimei.cflow.point.DataActors
import com.yimei.cflow.ying.YingSupervisor

import scala.concurrent.duration._


/**
  * Created by hary on 16/12/1.
  */
object Main extends App with Core{

  yingDemo()

  def yingDemo(flowId: String = UUID.randomUUID().toString) = {
    println("intialize system...")

    implicit val timeout = Timeout(5 seconds)
    implicit val executionContext = system.dispatcher

    // 启动仓押流程管理器
    val ying = system.actorOf(Props[YingSupervisor], "YingSupervisor")

    // 启动采集器...
    system.actorOf(DataActors.props(ying), "DataActors")


    //    ying ! CommandPoint(flowId, "R", DataPoint(50, "root memo", "hary", new Date()))

    val queryActor = system.actorOf(Props(new QueryActor(ying)), "queryActor")
    system.scheduler.schedule(0 seconds, 7 seconds, queryActor, CommandQuery(flowId))
  }

  class QueryActor(ying: ActorRef) extends Actor {
    def receive = {
      case CommandQuery(flowId) =>
        ying ! CommandQuery(flowId)
      case jsonGraph: String =>
        // system.log.info(s"$state")
        // context.system.log.info(s"收到消息json = $jsonGraph")
    }
  }

}
