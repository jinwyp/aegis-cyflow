package com.yimei.cflow.graph.cang.db

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.yimei.cflow.graph.cang.db.Entities.DepositEntity
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * Created by xl on 17/1/9.
  */
object Entities {
  case class DepositEntity(id: Option[Long], flowId: String, expectedAmount: BigDecimal, actuallyAmount: BigDecimal, state: String, memo: String, ts_c: Option[Timestamp])


}

trait DepositProtocol extends DefaultJsonProtocol {
  implicit object TimeStampJsonFormat extends RootJsonFormat[Timestamp] {


    override def write(obj: Timestamp) = {
      val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      JsString(formatter.format(obj))
    }

    override def read(json: JsValue): Timestamp = {
      val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
      json match {
        case JsString(s) => new Timestamp(formatter.parse(s).getTime)
        case _ => throw new DeserializationException("Error info you want here ...")
      }
    }
  }
  implicit val DepositEntityFormat = jsonFormat7(DepositEntity)
}
