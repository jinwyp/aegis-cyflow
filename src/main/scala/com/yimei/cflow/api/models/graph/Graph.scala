package com.yimei.cflow.api.models.graph

import com.yimei.cflow.api.models.flow._
import spray.json.DefaultJsonProtocol


case class Vertex(description: String, program: Option[String])

case class GraphConfig(
                        graphJar: String,
                        persistent: Boolean,
                        timeout: Int,
                        initial: String,
                        points: Map[String, String],
                        autoTasks: Map[String, TaskInfo],
                        userTasks: Map[String, TaskInfo],
                        vertices: Map[String, Vertex],
                        edges: Map[String, Edge]
                      )

trait GraphConfigProtocol extends FlowProtocol with DefaultJsonProtocol {
  implicit val defaultVertexFormat = jsonFormat2(Vertex)
  implicit val graphConfigProtocolFormat = jsonFormat9(GraphConfig)
}
