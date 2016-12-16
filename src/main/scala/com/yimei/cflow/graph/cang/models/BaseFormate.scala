package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp
import java.text.SimpleDateFormat

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * Created by wangqi on 16/12/16.
  */
object BaseFormatter extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object TimeStampJsonFormat extends RootJsonFormat[Timestamp] {

    val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override def write(obj: Timestamp) = JsString(formatter.format(obj))

    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => new Timestamp(formatter.parse(s).getTime)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
}
