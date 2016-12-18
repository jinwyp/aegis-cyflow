package com.yimei.cflow

import akka.actor.Props
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.{FlowRegistry, GraphLoader}
import com.yimei.cflow.graph.ying2.YingGraph
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.CorsSupport
import com.yimei.cflow.util.{TestClient, TestUtil}
import com.yimei.cflow.core.GraphLoader._

/**
  * Created by hary on 16/12/3.
  */
object ServiceTest extends App with ApplicationConfig with CorsSupport {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor


  // 1> 注册自动任务
  // AutoRegistry.register()

  // 2> 注册流程图
  loadall()
  //FlowRegistry.register(YingGraph.getFlowType, GraphLoader.kload)

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names, true), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")

  Thread.sleep(2000)

  //  TestUtil.test(proxy, client, "00", "hary","fund","wangqiId","fund","wqGroup")

  for (i <- 1 to 1) {
    TestUtil.test(proxy, client, s"00$i", s"hary$i", s"fund$i", s"wangqiId$i", s"fund$i", s"wqGroup$i")
  }

}

