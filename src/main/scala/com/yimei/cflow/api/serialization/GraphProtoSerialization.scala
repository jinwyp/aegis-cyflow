package com.yimei.cflow.api.serialization

import akka.serialization.SerializerWithStringManifest
import com.yimei.cflow.api.models.graph._

/**
  * Created by xl on 16/12/23.
  */
class GraphProtoSerialization extends SerializerWithStringManifest {
  override def identifier: Int = 2222

  val VertexManifest = classOf[Vertex].getName
  val GraphConfigManifest = classOf[GraphConfig].getName

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case VertexManifest => Vertex.parseFrom(bytes)
    case GraphConfigManifest => GraphConfig.parseFrom(bytes)
  }

  override def manifest(o: AnyRef): String = o.getClass.getName

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case e: Vertex => e.toByteArray
    case e: GraphConfig => e.toByteArray
  }
}
