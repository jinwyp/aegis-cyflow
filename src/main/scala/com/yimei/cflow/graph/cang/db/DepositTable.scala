package com.yimei.cflow.graph.cang.db

import java.sql.Timestamp

import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.graph.cang.db.Entities.DepositEntity

/**
  * Created by xl on 16/12/19.
  */
trait DepositTable {
  import driver.api._

  class Deposit(tag:Tag) extends Table[DepositEntity](tag,"deposit"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def flowId = column[String]("flowId")
    def expectedAmount = column[BigDecimal]("expectedAmount")
    def actuallyAmount = column[BigDecimal]("actuallyAmount")
    def state = column[String]("state")
    def memo = column[String]("memo")
    def ts_c = column[Option[Timestamp]]("ts_c")
    def * = (id,flowId,expectedAmount,actuallyAmount,state,memo,ts_c)<>(DepositEntity.tupled,DepositEntity.unapply)
  }

  protected val deposit = TableQuery[Deposit]
}
