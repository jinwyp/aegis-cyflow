package com.yimei.cflow.graph.cang.models

import java.time.LocalDateTime

case class FileObj(name: String, url: String, createTime: LocalDateTime)

case class FileObjList(fileList: List[FileObj])

/** 进入仓押系统,初始化 **/
case class StartFlow(applyUserId: BigInt, applyUserName: String, applyUserPhone: String, applyCompanyId: BigInt,
                     applyCompanyName: String, //申请人-融资方 信息
                     financeCreateTime: LocalDateTime,
                     financeEndTime: LocalDateTime,  //仓押开始,结束时间
                     downstreamCompanyName: String, financingAmount: BigDecimal, expectDate: Int, interestRate: BigDecimal,
                     coalType: String, coalIndex_NCV: Int, coalIndex_RS: BigDecimal, coalIndex_ADV: BigDecimal,
                     stockPort: String, coalAmount: BigDecimal, auditFileList: FileObjList, createTime: LocalDateTime)

/** 贸易商选择 港口,监管, 资金方 **/
case class TraffickerAssignUsers(portWorkerUserId: BigInt, portCompanyId: BigInt, supervisorWorkerUserId: BigInt, supervisorCompanyId: BigInt,
                                 fundProviderWorkerUserId: BigInt, fundProviderCompanyId: BigInt, fundProviderFinanceUserId: BigInt)

/** 融资方上传合同 **/
case class CustomerUploadFileList(contractFileList: FileObjList, financeFileList: FileObjList, businessFileList: FileObjList, createTime: LocalDateTime, createManId: BigInt)

