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

  val zjfyw = "fundProvider"
  val zjfcw = "fundProviderAccountant"
  
  //资金方业务组ID和财务组Id
  val fundGid = "1"
  val fundFinanceGid = "2"

  //tasks
  val a11SelectHarborAndSupervisor = "a11SelectHarborAndSupervisor"

  //points
  val startPoint = "startPoint"
  val orderId = "orderId"
  val traderUserId = "traderUserId"
  val traderAccountantUserId = "traderAccountantUserId"
  val harborUserId = "harborUserId"
  val supervisorUserId = "supervisorUserId"
  val fundProviderUserId = "fundProviderUserId"
  val fundProviderAccountantUserId = "fundProviderAccountantUserId"

}
