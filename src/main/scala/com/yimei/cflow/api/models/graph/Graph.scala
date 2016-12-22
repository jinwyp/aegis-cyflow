package com.yimei.cflow.api.models.graph

import com.yimei.cflow.core.Flow.{Arrow, Edge, TaskInfo}
import com.yimei.cflow.core.FlowProtocol
import spray.json.DefaultJsonProtocol


case class DefaultVertex(description: String, arrows: Seq[Arrow])

case class GraphConfig(
                        graphJar: String,
                        persistent: Boolean,
                        timeout: Int,
                        initial: String,
                        points: Map[String, String],
                        autoTasks: Map[String, TaskInfo],
                        userTasks: Map[String, TaskInfo],
                        vertices: Map[String, DefaultVertex],
                        edges: Map[String, Edge]
                      )

object GraphConfigProtocol extends DefaultJsonProtocol with FlowProtocol {
  implicit val defaultVertexFormat = jsonFormat2(DefaultVertex)
  implicit val graphConfigProtocolFormat = jsonFormat9(GraphConfig)
}
