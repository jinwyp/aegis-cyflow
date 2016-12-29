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

  //文件类型
  val default = "default"   //默认类型
  val contract = "contract" //合同
  val financeFile  = "financeFile" //财务文件
  val businessFile = "businessFile" //业务文件

  //参与方
  val rzf = "financer"
  val zjf = "fundProvider"
  val myf = "trader"
  val gkf = "harbor"
  val jgf = "supervisor"
  val adm = "systemAdmin"

  //流程类型
  val flowType = "cang"

  //初始化的用户UserId，贸易方，贸易方财务，管理员
  val myfUserId = "trader-88888888!77777"
  val myfFinanceId = "trader-88888888!88888"
  val adminId = "systemAdmin-00000000!00000"

  val zjfyw = "fundProvider"
  val zjfcw = "fundProviderAccountant"
  
  //资金方业务组ID和财务组Id
  val fundGid = "1"
  val fundFinanceGid = "2"

  //vertices
  val financingStep11 = "financingStep11"
  val financingStep12 = "financingStep12"
  val financingStep13 = "financingStep13"

  //edge
  val E1 = "E1"
  val E2 = "E2"

  //tasks
  val a11SelectHarborAndSupervisor = "a11SelectHarborAndSupervisor"
  val a12FinishedUpload = "a12FinishedUpload"
  val a13FinishedUpload = "a13FinishedUpload"
  val a14FinishedUpload = "a14FinishedUpload"

  //points
  val startPoint = "startPoint"
  val orderId = "orderId"
  val traderUserId = "traderUserId"
  val traderAccountantUserId = "traderAccountantUserId"
  val harborUserId = "harborUserId"
  val supervisorUserId = "supervisorUserId"
  val fundProviderUserId = "fundProviderUserId"
  val fundProviderAccountantUserId = "fundProviderAccountantUserId"

  val financerContractFiles = "financerContractFiles"
  val harborContractFiles = "harborContractFiles"
  val harborConfirmAmount = "harborConfirmAmount"
  val supervisorContractFiles = "supervisorContractFiles"

}
