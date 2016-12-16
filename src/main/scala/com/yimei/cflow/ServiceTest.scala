package com.yimei.cflow

import akka.actor.Props
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.{FlowGraph, FlowRegistry}
import com.yimei.cflow.graph.ying.YingGraph
import com.yimei.cflow.integration.{DaemonMaster, ServiceProxy}
import com.yimei.cflow.swagger.CorsSupport
import com.yimei.cflow.util.{TestClient, TestUtil}

/**
  * Created by hary on 16/12/3.
  */
object ServiceTest extends App with ApplicationConfig with CorsSupport {

  implicit val testTimeout = coreTimeout
  implicit val testEc = coreExecutor


  // 1> 注册自动任务
  // AutoRegistry.register()

  // 2> 注册流程图
  FlowRegistry.register(YingGraph.getFlowType, YingGraph)

  // daemon master and
  val names = Array(module_auto, module_user, module_flow, module_id, module_group)
  val daemon = coreSystem.actorOf(DaemonMaster.props(names, true), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")

  Thread.sleep(2000)

//  val client = coreSystem.actorOf(Props(new TestClient(proxy)), "TestClient")
//  TestUtil.test(proxy, client, "00", "hary","fund","wangqiId","fund","wqGroup")

  for (i <- 1 to 2) {
    // TestUtil.test(proxy, client, s"00-hary${i}")
    val client = coreSystem.actorOf(Props(new TestClient(proxy)), s"TestClient$i")
    TestUtil.test(proxy, client, s"00$i", s"hary$i",s"fund$i",s"wangqiId$i",s"fund$i",s"wqGroup$i")
  }

}

