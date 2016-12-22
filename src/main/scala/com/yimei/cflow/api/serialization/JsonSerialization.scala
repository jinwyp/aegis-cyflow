package com.yimei.cflow.api.serialization

import akka.serialization.SerializerWithStringManifest

/**
  * Created by hary on 16/12/21.
  */
class JsonSerialization extends SerializerWithStringManifest{

  override def identifier: Int = ???

  override def manifest(o: AnyRef): String = ???

  override def toBinary(o: AnyRef): Array[Byte] = ???

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = ???

}
