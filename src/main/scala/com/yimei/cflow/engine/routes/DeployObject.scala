package com.yimei.cflow.engine.routes

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol

object DeployObject extends DefaultJsonProtocol {
  import com.yimei.cflow.graph.cang.models.BaseFormatter.TimeStampJsonFormat

  case class SaveDeploy(id: Option[Long], flow_type: String, jar: String, enable: Boolean, ts_c: Option[Timestamp])
  implicit val saveDeployFormat = jsonFormat5(SaveDeploy)
}
