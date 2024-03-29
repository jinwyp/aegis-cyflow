package com.yimei.cflow.test

import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.DaemonMaster
import com.yimei.cflow.graph.ying.YingConfig._

/**
  * Created by hary on 16/12/16.
  */
object GroupTest extends App {

  val names = Array(module_auto, module_user, module_group, module_flow, module_id)

  // daemon master and
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  Thread.sleep(1000)

  // 测试group服务
  for {
    gc <- ServiceProxy.groupCreate(proxy, "operation", "risk")
  } {
    ServiceProxy.groupTask(proxy, "operation", "risk", "flowId", "helloTask", flow_ying)
    Thread.sleep(1000)

    for {
      gq <- ServiceProxy.groupQuery(proxy, "operation", "risk")
      uc <- ServiceProxy.userCreate(proxy, "operation", "hary")
      claim <- ServiceProxy.groupClaim(proxy, "operation", "risk", "hary", gq.tasks.head._1)
    } {
      println(s"gcreate create = $gc")
      println(s"gquery         = $gq")
      println(s"ucreate        = $uc")
      println(s"claim          = $claim")
      Thread.sleep(1000)
      for {
        uq <- ServiceProxy.userQuery(proxy, "operation", "hary")
      } {
        println(s"userQuery       = $uq")
      }
    }
  }

  Thread.sleep(2000)
  for {
    uq <- ServiceProxy.userQuery(proxy, "operation", "hary")
  } {
    println(s"userQuery       = $uq")
  }
}
