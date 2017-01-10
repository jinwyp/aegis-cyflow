package com.yimei.cflow.graph.cang.db

import java.sql.Timestamp

/**
  * Created by xl on 17/1/9.
  */
object Entities {
  case class DepositEntity(id: Option[Long], flowId: String, expectedAmount: BigDecimal, actuallyAmount: BigDecimal, state: String, memo: String, ts_c: Timestamp)
  case class CangPayTransactionEntity(id:Option[Long],
                                      flowId:String,
                                      pointName:String,
                                      srcUserType:String,
                                      srcUserId:String,
                                      srcCompanyId:String,
                                      targetUserType:String,
                                      targetUserId:String,
                                      targetCompanyId:String,
                                      amount:BigDecimal,
                                      transaction_id:Option[String],
                                      status:Int,
                                      message:Option[String]
                                      )
}
