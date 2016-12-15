package com.yimei.cflow.graph.cang.models

import java.time.LocalDateTime

case class FileObj(name: String, url: String, createTime: LocalDateTime)

case class FileObjList(fileList: List[FileObj])

/** 进入仓押系统,初始化 **/
case class startFlow(applyUserId: BigInt, applyUserName: String, applyUserPhone: String, applyCompanyId: BigInt,
                     applyCompanyName: String, financeCreateTime: LocalDateTime, financeEndTime: LocalDateTime,
                     downstreamCompanyName: String, financingAmount: BigDecimal, expectDate: Int, interestRate: BigDecimal,
                     coalType: String, coalIndex_NCV: Int, coalIndex_RS: BigDecimal, coalIndex_ADV: BigDecimal,
                     stockPort: String, coalAmount: BigDecimal, auditFileList: FileObjList, createTime: LocalDateTime)


