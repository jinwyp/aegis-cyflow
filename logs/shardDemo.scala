package com.yimei.cflow

///**
//  * Created by hary on 16/12/2.
//  */
//class shardDemo {
//  def persistDemo: Unit = {
//    import com.yimei.cflow.core.PersistentFlow.{Command, CommandQuery, DataPoint}
//    val persist = system.actorOf(Props[PersistSupervisor], "PersistSupervisor")
//    // 向仓押管理器发起创建
//    val pf = persist ? PersistRequest("hary")
//    pf.mapTo[PersistResponse] onSuccess {
//      case resp =>
//        system.log.info(s"创建持久流程成功: 流程id = ${resp.orderId}")
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
//}
