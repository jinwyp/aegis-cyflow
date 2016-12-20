package com.yimei.cflow.serialization

import akka.serialization.SerializerWithStringManifest

/**
  * Created by hary on 16/12/19.
  */
class ProtoSerialization extends SerializerWithStringManifest {

  override def identifier: Int = 12345678

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = ???

  override def manifest(o: AnyRef): String = ???

  override def toBinary(o: AnyRef): Array[Byte] = ???
}
