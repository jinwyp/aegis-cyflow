package com.yimei.cflow

import java.util.UUID

import akka.actor.Props
import com.yimei.cflow.cang.CangFlowMaster
import com.yimei.cflow.config.Core
import com.yimei.cflow.data.DataMaster
import com.yimei.cflow.integration.DaemonMaster
import com.yimei.cflow.integration.DaemonMaster.QueryTest
import com.yimei.cflow.user.UserMaster
import com.yimei.cflow.util.QueryActor
import com.yimei.cflow.ying.YingFlowMaster

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/3.
  */
object YingTest extends App with Core {

  // 仓押, 应收, 数据, 用户 4大模块
  var moduleProps = Map[String, Props](
    module_ying -> YingFlowMaster.props(),
    module_cang -> CangFlowMaster.props(),
    module_data -> DataMaster.props(),
    module_user -> UserMaster.props()
  )

  // 启动daemon master
  val daemon = system.actorOf(DaemonMaster.props(moduleProps), "DaemonMaster")

  val queryActor = system.actorOf(Props(new QueryActor(daemon)), "queryActor")

  val flowId = UUID.randomUUID().toString
  system.scheduler.scheduleOnce(1 seconds, queryActor, QueryTest(module_ying, flowId, "hary"))
}




