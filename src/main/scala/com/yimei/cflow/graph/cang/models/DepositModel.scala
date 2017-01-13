package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.yimei.cflow.graph.cang.models.DepositModel.AddDeposit
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * Created by xl on 17/1/10.
  */
object DepositModel extends DefaultJsonProtocol {
  case class AddDeposit(flowId: String, expectedAmount: BigDecimal, state: String, memo: Option[String])
  implicit val AddDepositFormat = jsonFormat4(AddDeposit)

  case class CompanyAuditQueryResponse(success: Boolean, error: Option[String])
  implicit val CompanyAuditQueryResponseFormat = jsonFormat2(CompanyAuditQueryResponse)
}

