package com.yimei.cflow

import java.util.UUID

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.yimei.cflow.cang.CangSupervisor
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow.CommandQuery
import com.yimei.cflow.point.DataActors
import com.yimei.cflow.ying.YingSupervisor

import scala.concurrent.duration._


/**
  * Created by hary on 16/12/1.
  */
object CangTest extends App with Core {


  // 运行cang demo
  cangDemo()

  def cangDemo(flowId: String = UUID.randomUUID().toString): Unit = {

    println("intialize system...")

    val system = ActorSystem("RiskSystem")

    implicit val timeout = Timeout(5 seconds)
    implicit val executionContext = system.dispatcher

    // 启动仓押流程管理器
    val cang = system.actorOf(Props[CangSupervisor], "CangSupervisor")

    // 启动采集器...
    system.actorOf(DataActors.props(cang), "DataActors")

    import com.yimei.cflow.core.Flow.CommandQuery

    val queryActor = system.actorOf(Props(new QueryActor(cang)), "queryActor")
    system.scheduler.schedule(2 seconds, 13 seconds, queryActor, CommandQuery(flowId))

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
