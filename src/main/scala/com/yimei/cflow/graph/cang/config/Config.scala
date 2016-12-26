package com.yimei.cflow.graph.cang.config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

/**
  * Created by wangqi on 16/12/26.
  */
trait Config {
  implicit val coreSystem = ActorSystem("ClientSystem")
  implicit val coreExecutor = coreSystem.dispatcher
  implicit val coreMaterializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  val url = config.getString("server.url")
  val port = config.getInt("client.port")

  val rzf = "rzf"
  val flowType = "cang"

}
