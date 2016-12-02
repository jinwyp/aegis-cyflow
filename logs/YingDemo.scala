package com.yimei.cflow

import java.util.Date

import akka.actor.{Actor, Props}

/**
  * Created by hary on 16/12/2.
  */
//object YingDemo {
//
//  def yingDemo {
//    import com.yimei.cflow.core.Flow.{Command, CommandQuery, DataPoint}
//    // 启动应收流程管理器
//    val ying = system.actorOf(Props[YingSupervisor], "YingSupervisor")
//
//    // 向应收管理器发起创建
//    val yf = ying ? YingRequest("hary")
//    yf.mapTo[YingResponse] onSuccess {
//      case resp =>
//        system.log.info(s"创建应收流程成功: 流程id = ${resp.orderId}")
//        resp.flowRef ! Command("R", DataPoint(50, "memo", "hary", new Date()))
//        system.scheduler.schedule(2 seconds, 2 seconds, system.actorOf(Props(new Actor {
//          def receive = {
//            case CommandQuery =>
//              resp.flowRef ! CommandQuery
//            case jsonGraph: String =>
//              // system.log.info(s"$state")
//              system.log.info(s"收到消息json = $jsonGraph")
//          }
//        })), CommandQuery)
//    }
//  }
//
//}
