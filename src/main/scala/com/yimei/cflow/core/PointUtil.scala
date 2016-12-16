package com.yimei.cflow.core

import java.util.UUID

import com.yimei.cflow.core.Flow.DataPoint
import spray.json._


object PointUtil {

  case class PointModel[T](value: T, memo: Option[String], operator: Option[String], id: String, timestamp: Long)
  /**
    * Created by hary on 16/12/15.
    */
  implicit class str2object(str: String) {
    def as[A: JsonFormat] = str.parseJson.convertTo[A]
  }

  implicit class object2str[A:JsonFormat](o: A){
    def str = o.toJson.toString()
  }

  implicit class dataPointUnWrapper(dp: DataPoint) {
    def unwrap[A:JsonFormat] = PointModel[A]( dp.value.parseJson.convertTo[A], dp.memo, dp.operator, UUID.randomUUID().toString, 0L)
  }

  implicit class dataPointWrapper[A:JsonFormat](a: A) {
    def wrap[A](memo: Option[String] = None, operator: Option[String] = None) = DataPoint(a.str, memo, operator, UUID.randomUUID().toString, 0L, false)
  }
}
