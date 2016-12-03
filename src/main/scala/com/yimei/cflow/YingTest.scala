package com.yimei.cflow

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import com.yimei.cflow.cang.CangFlowMaster
import com.yimei.cflow.config.Core
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.integration.DaemonMaster
import com.yimei.cflow.integration.DaemonMaster.QueryTest
import com.yimei.cflow.ying.YingFlowMaster

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/3.
  */
object YingTest extends App with Core {

  // 仓押, 应收, 数据, 用户 4大模块
  var moduleProps = Map[String, Props](
    module_cang -> CangFlowMaster.props(),
    module_ying -> YingFlowMaster.props(),
    module_data -> DataMaster.props()
  )

  // 启动daemon master
  val daemon = system.actorOf(DaemonMaster.props(moduleProps), "DaemonMaster")

  val queryActor = system.actorOf(Props(new QueryActor(daemon)), "queryActor")
  system.scheduler.schedule(2 seconds, 13 seconds, queryActor, QueryTest(module_ying, UUID.randomUUID().toString))
}

class QueryActor(daemon: ActorRef) extends Actor {
  def receive = {
    case test: QueryTest =>
      daemon ! test
    case jsonGraph: String =>
     context.system.log.info(s"收到消息json = $jsonGraph")
  }
}


