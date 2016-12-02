package com.yimei.cflow

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.util.Timeout
import com.yimei.cflow.cang.CangSupervisor
import com.yimei.cflow.core.Flow.{CommandPoint, CommandQuery, DataPoint}
import com.yimei.cflow.point.DataActors
import com.yimei.cflow.ying.YingSupervisor

import scala.concurrent.duration._


/**
  * Created by hary on 16/12/1.
  */
object Main extends App {


  // 运行cang demo
  // cangDemo()
  yingDemo()
  // yingDemo("0406c0df-d13a-4711-90f0-9ed6c16eff05")

  ////////////////////////////////////////////////////////////////////////////
  //   测试代码!!!!
  ////////////////////////////////////////////////////////////////////////////
  def cangDemo(flowId: String = UUID.randomUUID().toString): Unit = {

    println("intialize system...")

    val system = ActorSystem("RiskSystem")

    implicit val timeout = Timeout(5 seconds)
    implicit val executionContext = system.dispatcher

    // 启动采集器...
    system.actorOf(Props[DataActors], "DataActors")

    import com.yimei.cflow.core.Flow.CommandQuery

    // 启动仓押流程管理器
    val cang = system.actorOf(Props[CangSupervisor], "CangSupervisor")

    cang ! CommandPoint(flowId, "R", DataPoint(50, "root memo", "hary", new Date()))

    val queryActor = system.actorOf(Props(new QueryActor(cang)), "queryActor")
    system.scheduler.schedule(2 seconds, 13 seconds, queryActor, CommandQuery(flowId))

  }

  def yingDemo(flowId: String = UUID.randomUUID().toString) = {
    println("intialize system...")

    val system = ActorSystem("RiskSystem")

    implicit val timeout = Timeout(5 seconds)
    implicit val executionContext = system.dispatcher

    // 启动采集器...
    system.actorOf(Props[DataActors], "DataActors")

    // 启动仓押流程管理器
    val ying = system.actorOf(Props[YingSupervisor], "YingSupervisor")

    ying ! CommandPoint(flowId, "R", DataPoint(50, "root memo", "hary", new Date()))

    val queryActor = system.actorOf(Props(new QueryActor(ying)), "queryActor")
    system.scheduler.schedule(2 seconds, 13 seconds, queryActor, CommandQuery(flowId))
  }

  class QueryActor(ying: ActorRef) extends Actor {
    def receive = {
      case CommandQuery(flowId) =>
        ying ! CommandQuery(flowId)
      case jsonGraph: String =>
        // system.log.info(s"$state")
        context.system.log.info(s"收到消息json = $jsonGraph")
    }
  }

}
