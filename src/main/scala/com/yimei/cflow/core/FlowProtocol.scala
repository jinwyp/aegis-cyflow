package com.yimei.cflow.core

import java.text.SimpleDateFormat
import java.util.Date

import com.yimei.cflow.core.Flow._
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * Created by hary on 16/12/1.
  */
trait FlowProtocol extends DefaultJsonProtocol {

  // 日期
  implicit object DateJsonFormat extends RootJsonFormat[Date] {
    val formatter = new SimpleDateFormat("yyyy-MM-dd")   // todo change format

    override def write(obj: Date) = JsString(formatter.format(obj))

    override def read(json: JsValue): Date = json match {
      case JsString(s) => formatter.parse(s)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }

  // 序列化Decision
  implicit object DecisionFormat extends RootJsonFormat[Decision] {
    def write(c: Decision) = JsString(c.toString)

    def read(value: JsValue) = null
  }

  // 序列Edge
  implicit object EdgeFormat extends RootJsonFormat[Edge] {
    def write(c: Edge) = JsString(c.toString)

    def read(value: JsValue) = null
  }

  // 泛型数据点
  //implicit def dataPointFormat[T:JsonFormat] = jsonFormat5(DataPoint.apply[T])
  implicit val dataPointFormat = jsonFormat5(DataPoint)

  implicit val arrowFormat =jsonFormat2(Arrow)

  implicit val stateFormat = jsonFormat6(State)

  implicit val graphFormat = jsonFormat3(Graph)
}
