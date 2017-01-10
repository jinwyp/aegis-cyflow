package com.yimei.cflow.graph.cang.db
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.graph.cang.db.Entities.CangPayTransactionEntity

/**
  * Created by wangqi on 17/1/10.
  */
trait CangPayTransactionTable {
  import driver.api._

  class CangPayTransaction(tag:Tag) extends Table[CangPayTransactionEntity](tag,"cang_pay_transaction") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def flowId = column[String]("flow_id")
    def pointName = column[String]("point_name")
    def srcUserType = column[String]("src_user_type")
    def srcUserId = column[String]("src_user_id")
    def srcCompanyId = column[String]("src_company_id")
    def targetUserType = column[String]("target_user_type")
    def targetUserId = column[String]("target_user_id")
    def targetCompanyId = column[String]("target_company_id")
    def amount = column[BigDecimal]("amount")
    def transactionId = column[Option[String]]("transaction_id")
    def status = column[Int]("status")
    def message = column[Option[String]]("message")

    def * = (id,flowId,pointName,srcUserType,srcUserId,srcCompanyId,targetUserType,
      targetUserId,targetCompanyId,amount,transactionId,status,message) <> (CangPayTransactionEntity.tupled,CangPayTransactionEntity.unapply)
  }

  protected val cangPayTransaction = TableQuery[CangPayTransaction]

}
