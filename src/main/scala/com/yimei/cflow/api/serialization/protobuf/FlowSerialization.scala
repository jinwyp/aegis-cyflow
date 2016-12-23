package com.yimei.cflow.api.serialization.protobuf

import akka.serialization.SerializerWithStringManifest

/**
  * Created by hary on 16/12/23.
  */
class FlowSerialization extends SerializerWithStringManifest{

  override def identifier: Int = ???

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = ???

  override def manifest(o: AnyRef): String = ???

  override def toBinary(o: AnyRef): Array[Byte] = ???

}
