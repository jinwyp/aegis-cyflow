package com.yimei.cflow.config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import concurrent.duration._

/**
  * Created by hary on 16/12/2.
  */
trait CoreConfig {
  implicit val coreSystem = ActorSystem("RiskSystem")
  implicit val coreTimeout = Timeout(5 seconds)
  implicit val coreExecutor = coreSystem.dispatcher
  implicit val coreMaterializer = ActorMaterializer()

  val config = coreSystem.settings.config

  val autoPoint = config.getConfig("autopoint")   // 自动任务数据点
  val taskPoint = config.getConfig("taskpoint")   // 用户任务数据点
}
