package com.yimei.cflow.graph.cang.config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory

/**
  * Created by wangqi on 16/12/26.
  */
object Config {
  implicit val coreSystem = ActorSystem("ClientSystem")
  implicit val coreExecutor = coreSystem.dispatcher
  implicit val coreMaterializer = ActorMaterializer()

  private val config = ConfigFactory.load()
  val url = config.getString("server.url")
  val port = config.getInt("client.port")

  val rzf = "rzf"
  val zjf = "zjf"
  val myf = "myf"
  val gkf = "gkf"
  val jgf = "jgf"
  val adm = "adm"

  val flowType = "cang"

  val zjfUserId = "zjf-88888888!77777"
  val zjfFinanceId = "zjf-88888888!88888"
  val adminId = "adm-00000000!00000"

}
