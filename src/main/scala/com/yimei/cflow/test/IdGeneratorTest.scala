package com.yimei.cflow.test

import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}

/**
  * Created by hary on 16/12/16.
  */
object IdGeneratorTest extends App with CoreConfig {

  val names = Array(module_id)

  // daemon master and
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  Thread.sleep(1000)

  // 测试Id
  val f = ServiceProxy.idGet(proxy, "hello")
  f.onSuccess {
    case s => println(s"got $s")
  }
}
