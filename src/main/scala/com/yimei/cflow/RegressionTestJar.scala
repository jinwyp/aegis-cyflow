package com.yimei.cflow

import akka.actor.Props
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.{DaemonMaster, GraphLoader}
import com.yimei.cflow.swagger.CorsSupport
import com.yimei.cflow.util.{TestClient, TestUtil}
import scala.sys.process._

/**
  * Created by hary on 16/12/3.
  */
object RegressionTestJar extends App with ApplicationConfig with CorsSupport {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor

  "rm -fr data/journal/*".!
  "rm -fr data/snapshots/*".!

  GraphLoader.loadall()

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000)

  //  TestUtil.test(proxy, client, "00", "hary","fund","wangqiId","fund","wqGroup")

  for (i <- 1 to 1) {
    TestUtil.test("ying", proxy, client, s"00$i", s"hary$i", s"fund$i", s"wangqiId$i", s"fund$i", s"wqGroup$i")
  }

}



