package com.yimei.cflow.api.serialization.protobuf

import akka.serialization.SerializerWithStringManifest
import com.yimei.cflow.api.models.id._


/**
  * Created by xl on 16/12/23.
  */
class IdProtoSerialization extends SerializerWithStringManifest {
  override def identifier: Int = 4444

  val CommandGetIdManifest = classOf[CommandGetId].getName
  val IdManifest = classOf[Id].getName
  val EventIncreaseManifest = classOf[EventIncrease].getName
  val StateManifest = classOf[State].getName

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case CommandGetIdManifest => CommandGetId.parseFrom(bytes)
    case IdManifest => Id.parseFrom(bytes)
    case EventIncreaseManifest => EventIncrease.parseFrom(bytes)
    case StateManifest => State.parseFrom(bytes)
  }

  override def manifest(o: AnyRef): String = o.getClass.getName

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case e: CommandGetId => e.toByteArray
    case e: Id => e.toByteArray
    case e: EventIncrease => e.toByteArray
    case e: State => e.toByteArray
  }
}
