package com.yimei.cflow

import java.util.Date

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.Flow.{Command, CommandQuery, DataPoint}
import com.yimei.cflow.cang.Cang.{CreateFlowRequest, CreateFlowResponse}
import com.yimei.cflow.cang.CangSupervisor

import scala.concurrent.duration._


/**
  * Created by hary on 16/12/1.
  */
object Main extends App {

  println("intialize system...")

  val system = ActorSystem("RiskSystem")

  implicit val timeout = Timeout(5 seconds)
  implicit val executionContext = system.dispatcher

  // 启动采集器...
  system.actorOf(Props[DataActors], "DataActors")

  // 启动仓押流程管理器
  val cang = system.actorOf(Props[CangSupervisor], "CangSupervisor")

  // 向管理器发起创建
  val f = cang ? CreateFlowRequest("hary")

  f.mapTo[CreateFlowResponse] onSuccess {
    case resp =>
      system.log.info(s"创建仓押流程成功: 流程id = ${resp.orderId}")
      resp.flowRef ! Command("R", DataPoint(50, "memo", "hary", new Date()))
      system.scheduler.schedule(2 seconds, 2 seconds, system.actorOf(Props(new Actor {

        def receive = {
          case CommandQuery =>
            resp.flowRef ! CommandQuery

          case jsonGraph: String =>
            // system.log.info(s"$state")
            system.log.info(s"收到消息json = $jsonGraph")
        }
      })), CommandQuery)
  }
}

