package com.yimei.cflow.graph.cang.db

import java.sql.Timestamp

/**
  * Created by xl on 17/1/9.
  */
object Entities {
  case class DepositEntity(id: Option[Long], flowId: String, expectedAmount: BigDecimal, actuallyAmount: BigDecimal, state: String, memo: String, ts_c: Timestamp)
}
