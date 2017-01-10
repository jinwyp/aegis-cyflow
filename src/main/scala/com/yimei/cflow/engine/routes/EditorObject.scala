package com.yimei.cflow.engine.routes


import java.sql.Timestamp

import spray.json.DefaultJsonProtocol

object EditorObject extends DefaultJsonProtocol {
  import com.yimei.cflow.graph.cang.models.BaseFormatter.TimeStampJsonFormat

  case class SaveDesign(id: Option[Long], name: String, json: Option[String], meta: Option[String])
  implicit val addDesignFormat = jsonFormat4(SaveDesign)

  case class DesignDetail(id: Long, name: String, json: Option[String], meta: Option[String], ts_c: Timestamp)
  implicit val designDetailFormat = jsonFormat5(DesignDetail)

  case class DesignList(id: Long, name: String, ts_c: Timestamp)
  implicit val designListFormat = jsonFormat3(DesignList)

}
