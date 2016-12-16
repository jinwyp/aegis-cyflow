package com.yimei.cflow.graph.cang.models

import java.time.LocalDateTime

import spray.json.DefaultJsonProtocol

object xxxx extends DefaultJsonProtocol {

  case class FileObj(name: String, url: String, createTime: Option[LocalDateTime])
  implicit val fileObjFormat = jsonFormat3(FileObj)

  /** 进入仓押系统,初始化 **/
  case class StartFlow(applyUserId: BigInt, applyUserName: String, applyUserPhone: String, applyCompanyId: BigInt,
                       applyCompanyName: String, financeCreateTime: LocalDateTime, financeEndTime: LocalDateTime,
                       downstreamCompanyName: String, financingAmount: BigDecimal, expectDate: Int, interestRate: BigDecimal,
                       coalType: String, coalIndex_NCV: Int, coalIndex_RS: BigDecimal, coalIndex_ADV: BigDecimal,
                       stockPort: String, coalAmount: BigDecimal, auditFileList: List[FileObj], createTime: LocalDateTime)

  /** 贸易商选择 港口,监管, 资金方 **/
  case class TraffickerAssignUsers(portWorkerUserId: BigInt, portCompanyId: BigInt, supervisorWorkerUserId: BigInt, supervisorCompanyId: BigInt,


                                   fundProviderWorkerUserId: BigInt, fundProviderCompanyId: BigInt, fundProviderFinanceUserId: BigInt)

  //////////////////////////////////////////////////////////////
  //
  //////////////////////////////////////////////////////////////


  //////////////////////////////////////////////////////////////
  //
  //////////////////////////////////////////////////////////////
}


