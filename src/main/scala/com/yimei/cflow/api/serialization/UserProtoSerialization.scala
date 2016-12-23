package com.yimei.cflow.api.serialization

import akka.serialization.SerializerWithStringManifest
import com.yimei.cflow.api.models.user._

/**
  * Created by xl on 16/12/23.
  */
class UserProtoSerialization extends SerializerWithStringManifest {
  override def identifier: Int = 5555

  val CommandCreateUserManifest = classOf[CommandCreateUser].getName
  val CommandTaskSubmitManifest = classOf[CommandTaskSubmit].getName
  val CommandShutDownManifest = classOf[CommandShutDown].getName
  val CommandMobileComeManifest = classOf[CommandMobileCome].getName
  val CommandDesktopComeManifest = classOf[CommandDesktopCome].getName
  val CommandQueryUserManifest = classOf[CommandQueryUser].getName
  val CommandUserTaskManifest = classOf[CommandUserTask].getName
  val TaskEnqueueManifest = classOf[TaskEnqueue].getName
  val TaskDequeueManifest = classOf[TaskDequeue].getName
  val StateManifest = classOf[State].getName

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case CommandCreateUserManifest => CommandCreateUser.parseFrom(bytes)
    case CommandTaskSubmitManifest => CommandTaskSubmit.parseFrom(bytes)
    case CommandShutDownManifest => CommandShutDown.parseFrom(bytes)
    case CommandMobileComeManifest => CommandMobileCome.parseFrom(bytes)
    case CommandDesktopComeManifest => CommandDesktopCome.parseFrom(bytes)
    case CommandQueryUserManifest => CommandQueryUser.parseFrom(bytes)
    case CommandUserTaskManifest => CommandUserTask.parseFrom(bytes)
    case TaskEnqueueManifest => TaskEnqueue.parseFrom(bytes)
    case TaskDequeueManifest => TaskDequeue.parseFrom(bytes)
    case StateManifest => State.parseFrom(bytes)
  }

  override def manifest(o: AnyRef): String = o.getClass.getName

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case e: CommandCreateUser => e.toByteArray
    case e: CommandTaskSubmit => e.toByteArray
    case e: CommandShutDown => e.toByteArray
    case e: CommandMobileCome => e.toByteArray
    case e: CommandDesktopCome => e.toByteArray
    case e: CommandQueryUser => e.toByteArray
    case e: CommandUserTask => e.toByteArray
    case e: TaskEnqueue => e.toByteArray
    case e: TaskDequeue => e.toByteArray
    case e: State => e.toByteArray
  }
}

