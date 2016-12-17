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

  implicit val partUTaskFormat = jsonFormat2(PartUTask)

  implicit val partGTaskFormat = jsonFormat2(PartGTask)

  implicit val edgeFormat = jsonFormat5(Edge)

  implicit val dataPointFormat = jsonFormat6(DataPoint)

  implicit val arrowFormat =jsonFormat2(Arrow)

  implicit val stateFormat = jsonFormat7(State)

  implicit val edgeDescriptionFormat = jsonFormat6(EdgeDescription)

  implicit val graphFormat = jsonFormat3(Graph)


}
