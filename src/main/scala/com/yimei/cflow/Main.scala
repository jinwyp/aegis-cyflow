package com.yimei.cflow

import java.util.Date

import akka.actor.{Actor, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.point.DataActors
import com.yimei.cflow.cang.Cang.{CreateFlowRequest => CangRequest, CreateFlowResponse => CangResponse}
import com.yimei.cflow.ying.Ying.{CreateFlowRequest => YingRequest, CreateFlowResponse => YingResponse}
import com.yimei.cflow.cang.CangSupervisor
import com.yimei.cflow.ying.YingSupervisor

import scala.concurrent.duration._


/**
  * Created by hary on 16/12/1.
  */
object Main extends App {



  // 运行cang demo
  //cangDemo

  // 运行ying demo
  yingDemo

  ////////////////////////////////////////////////////////////////////////////
  //   测试代码!!!!
  ////////////////////////////////////////////////////////////////////////////
  def cangDemo: Unit = {

    println("intialize system...")

    val system = ActorSystem("RiskSystem")

    implicit val timeout = Timeout(5 seconds)
    implicit val executionContext = system.dispatcher

    // 启动采集器...
    system.actorOf(Props[DataActors], "DataActors")

    import com.yimei.cflow.core.Flow.{CommandPoint, CommandQuery, DataPoint}

    // 启动仓押流程管理器
    val cang = system.actorOf(Props[CangSupervisor], "CangSupervisor")

    // 向仓押管理器发起创建
    val cf = cang ? CangRequest("hary")

    cf.mapTo[CangResponse] onSuccess {
      case resp =>
        system.log.info(s"创建仓押流程成功: 流程id = ${resp.flowId}")
        resp.flowRef ! CommandPoint(resp.flowId, "R", DataPoint(50, "memo", "hary", new Date()))
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

  def yingDemo: Unit = {

    println("intialize system...")

    val system = ActorSystem("RiskSystem")

    implicit val timeout = Timeout(5 seconds)
    implicit val executionContext = system.dispatcher

    // 启动采集器...
    system.actorOf(Props[DataActors], "DataActors")

    import com.yimei.cflow.core.Flow.{CommandPoint, CommandQuery, DataPoint}

    // 启动仓押流程管理器
    val ying = system.actorOf(Props[YingSupervisor], "CangSupervisor")

    // 向仓押管理器发起创建
    val cf = ying ? YingRequest("hary")

    cf.mapTo[YingResponse] onSuccess {
      case resp =>
        system.log.info(s"创建应收流程成功: 流程id = ${resp.flowId}")
        resp.flowRef ! CommandPoint(resp.flowId, "R", DataPoint(50, "memo", "hary", new Date()))
        system.scheduler.schedule(2 seconds, 2 seconds, system.actorOf(Props(new Actor {
          def receive = {
            case CommandQuery =>
              resp.flowRef ! CommandQuery(resp.flowId)
            case jsonGraph: String =>
              // system.log.info(s"$state")
              system.log.info(s"收到消息json = $jsonGraph")
          }
        })), CommandQuery)
    }
  }


}

