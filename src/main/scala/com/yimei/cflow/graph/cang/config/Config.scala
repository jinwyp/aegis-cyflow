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

  val rzf = "financer"
  val zjf = "fundProvider"
  val myf = "trader"
  val gkf = "harbor"
  val jgf = "supervisor"
  val adm = "systemAdmin"

  val flowType = "cang"

  val myfUserId = "trader-88888888!77777"
  val myfFinanceId = "trader-88888888!88888"
  val adminId = "systemAdmin-00000000!00000"


  //tasks
  val a11SelectHarborAndSupervisor = "a11SelectHarborAndSupervisor"

}
