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
  val financingStep14 = "financingStep14"
  val financingStep15 = "financingStep15"
  val financingStep16 = "financingStep16"
  val financingStep17 = "financingStep17"
  val financingStep18 = "financingStep18"
  val financingStep19 = "financingStep19"
  val repaymentStep20 = "repaymentStep20"
  val repaymentStep21 = "repaymentStep21"
  val repaymentStep22 = "repaymentStep22"
  val repaymentStep23 = "repaymentStep23"
  val repaymentStep24 = "repaymentStep24"
  val repaymentStep25 = "repaymentStep25"
  val repaymentStep26 = "repaymentStep26"


  //edge
  val E1 = "E1"
  val E2 = "E2"
  val E3 = "E3"
  val E4 = "E4"
  val E5 = "E5"
  val E6 = "E6"
  val E7 = "E7"
  val E8 = "E8"
  val E9 = "E9"
  val E10 = "E10"
  val E11 = "E11"
  val E12 = "E12"
  val E13 = "E13"
  val E14 = "E14"
  val E15 = "E15"
  val E16 = "E16"



  //tasks
  val a11SelectHarborAndSupervisor = "a11SelectHarborAndSupervisor"
  val a12FinishedUpload = "a12FinishedUpload"
  val a13FinishedUpload = "a13FinishedUpload"
  val a14FinishedUpload = "a14FinishedUpload"
  val a15traderAudit    = "a15traderAudit"
  val a16traderRecommendAmount = "a16traderRecommendAmount"
  val a17fundProviderAudit = "a17fundProviderAudit"
  val a18fundProviderAccountantAudit = "a18fundProviderAccountantAudit"
  val fundProviderPayingTask = "fundProviderPayingTask" //auto task
  val traderPayingTask = "traderPayingTask"              //auto task
  val a19SecondReturnMoney = "a19SecondReturnMoney"
  val financerPayingTask = "financerPayingTask"
  val a20noticeHarborRelease = "a20noticeHarborRelease"
  val a21harborRelease = "a21harborRelease"
  val a22traderAuditIfComplete = "a22traderAuditIfComplete"
  val a23ReturnMoney = "a23ReturnMoney"
  val a24AccountantReturnMoney = "a24AccountantReturnMoney"



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

  val traderAuditResult = "traderAuditResult"
  val fundProviderInterestRate = "fundProviderInterestRate"

  val recommendAmount = "recommendAmount"

  val fundProviderAuditResult = "fundProviderAuditResult"

  val fundProviderAccountantAuditResult = "fundProviderAccountantAuditResult"

  val fundProviderPaying = "fundProviderPaying"
  val fundProviderPaySuccess = "fundProviderPaySuccess"

  val traderPaying = "traderPaying"
  val traderPaySuccess = "traderPaySuccess"

  val repaymentAmount = "repaymentAmount"

  val financerPaying = "financerPaying"
  val financerPaySuccess = "financerPaySuccess"

  val traderNoticeHarborRelease = "traderNoticeHarborRelease"

  val harborReleaseGoods = "harborReleaseGoods"


  val TraderAuditIfCompletePayment = "TraderAuditIfCompletePayment"

  val TraderConfirmPayToFundProvider = "TraderConfirmPayToFundProvider"

  val TraderAccountantConfirm = "TraderAccountantConfirm"
}
