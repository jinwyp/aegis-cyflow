package com.yimei.cflow.api.serialization

import akka.serialization.SerializerWithStringManifest
import com.yimei.cflow.api.models.group._

/**
  * Created by xl on 16/12/23.
  */
class GroupProtoSerialization extends SerializerWithStringManifest {
  override def identifier: Int = 3333

  val CommandCreateGroupManifest = classOf[CommandCreateGroup].getName
  val CommandGroupTaskManifest = classOf[CommandGroupTask].getName
  val CommandClaimTaskManifest = classOf[CommandClaimTask].getName
  val CommandQueryGroupManifest = classOf[CommandQueryGroup].getName
  val TaskEnqueueManifest = classOf[TaskEnqueue].getName
  val TaskDequeueManifest = classOf[TaskDequeue].getName
  val StateManifest = classOf[State].getName

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case CommandCreateGroupManifest => CommandCreateGroup.parseFrom(bytes)
    case CommandGroupTaskManifest => CommandGroupTask.parseFrom(bytes)
    case CommandClaimTaskManifest => CommandClaimTask.parseFrom(bytes)
    case CommandQueryGroupManifest => CommandQueryGroup.parseFrom(bytes)
    case TaskEnqueueManifest => TaskEnqueue.parseFrom(bytes)
    case TaskDequeueManifest => TaskDequeue.parseFrom(bytes)
    case StateManifest => State.parseFrom(bytes)}

  override def manifest(o: AnyRef): String = o.getClass.getName

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case e: CommandCreateGroup => e.toByteArray
    case e: CommandGroupTask => e.toByteArray
    case e: CommandClaimTask => e.toByteArray
    case e: CommandQueryGroup => e.toByteArray
    case e: TaskEnqueue => e.toByteArray
    case e: TaskDequeue => e.toByteArray
    case e: State => e.toByteArray
  }

}
