package com.yimei.cflow.api.serialization

import akka.serialization.SerializerWithStringManifest
import com.yimei.cflow.api.models.flow._

/**
  * Created by xl on 16/12/23.
  */
class FlowProtoSerialization extends SerializerWithStringManifest {
  override def identifier: Int = 1111

  val DataPointManifest = classOf[DataPoint].getName
  val CommandCreateFlowManifest = classOf[CommandCreateFlow].getName
  val CreateFlowSuccessManifest = classOf[CreateFlowSuccess].getName
  val CommandRunFlowManifest = classOf[CommandRunFlow].getName
  val CommandShutdownManifest = classOf[CommandShutdown].getName
  val CommandPointManifest = classOf[CommandPoint].getName
  val CommandPointsManifest = classOf[CommandPoints].getName
  val CommandFlowGraphManifest = classOf[CommandFlowGraph].getName
  val CommandFlowStateManifest = classOf[CommandFlowState].getName
  val CommandUpdatePointsManifest = classOf[CommandUpdatePoints].getName
  val CommandHijackManifest = classOf[CommandHijack].getName
  val PointUpdatedManifest = classOf[PointUpdated].getName
  val PointsUpdatedManifest = classOf[PointsUpdated].getName
  val EdgeCompletedManifest = classOf[EdgeCompleted].getName
  val DecisionUpdatedManifest = classOf[DecisionUpdated].getName
  val HijackedManifest = classOf[Hijacked].getName
  val StateManifest = classOf[State].getName
  val PartUTaskManifest = classOf[PartUTask].getName
  val PartGTaskManifest = classOf[PartGTask].getName
  val EdgeManifest = classOf[Edge].getName
  val TaskInfoManifest = classOf[TaskInfo].getName
  val GraphManifest = classOf[Graph].getName
  val ArrowManifest = classOf[Arrow].getName

  override def fromBinary(bytes: Array[Byte], manifest: String): AnyRef = manifest match {
    case DataPointManifest => DataPoint.parseFrom(bytes)
    case CommandCreateFlowManifest => CommandCreateFlow.parseFrom(bytes)
    case CreateFlowSuccessManifest => CreateFlowSuccess.parseFrom(bytes)
    case CommandRunFlowManifest => CommandRunFlow.parseFrom(bytes)
    case CommandShutdownManifest => CommandShutdown.parseFrom(bytes)
    case CommandPointManifest => CommandPoint.parseFrom(bytes)
    case CommandPointsManifest => CommandPoints.parseFrom(bytes)
    case CommandFlowGraphManifest => CommandFlowGraph.parseFrom(bytes)
    case CommandFlowStateManifest => CommandFlowState.parseFrom(bytes)
    case CommandUpdatePointsManifest => CommandUpdatePoints.parseFrom(bytes)
    case CommandHijackManifest => CommandHijack.parseFrom(bytes)
    case PointUpdatedManifest => PointUpdated.parseFrom(bytes)
    case PointsUpdatedManifest => PointsUpdated.parseFrom(bytes)
    case EdgeCompletedManifest => EdgeCompleted.parseFrom(bytes)
    case DecisionUpdatedManifest => DecisionUpdated.parseFrom(bytes)
    case HijackedManifest => Hijacked.parseFrom(bytes)
    case StateManifest => State.parseFrom(bytes)
    case PartUTaskManifest => PartUTask.parseFrom(bytes)
    case PartGTaskManifest => PartGTask.parseFrom(bytes)
    case EdgeManifest => Edge.parseFrom(bytes)
    case TaskInfoManifest => TaskInfo.parseFrom(bytes)
    case GraphManifest => Graph.parseFrom(bytes)
    case ArrowManifest => Arrow.parseFrom(bytes)
  }

  override def manifest(o: AnyRef): String = o.getClass.getName

  override def toBinary(o: AnyRef): Array[Byte] = o match {
    case e: DataPoint => e.toByteArray
    case e: CommandCreateFlow => e.toByteArray
    case e: CreateFlowSuccess => e.toByteArray
    case e: CommandRunFlow => e.toByteArray
    case e: CommandShutdown => e.toByteArray
    case e: CommandPoint => e.toByteArray
    case e: CommandPoints => e.toByteArray
    case e: CommandFlowGraph => e.toByteArray
    case e: CommandFlowState => e.toByteArray
    case e: CommandUpdatePoints => e.toByteArray
    case e: CommandHijack => e.toByteArray
    case e: PointUpdated => e.toByteArray
    case e: PointsUpdated => e.toByteArray
    case e: EdgeCompleted => e.toByteArray
    case e: DecisionUpdated => e.toByteArray
    case e: Hijacked => e.toByteArray
    case e: State => e.toByteArray
    case e: PartUTask => e.toByteArray
    case e: PartGTask => e.toByteArray
    case e: Edge => e.toByteArray
    case e: TaskInfo => e.toByteArray
    case e: Graph => e.toByteArray
    case e: Arrow => e.toByteArray
  }
}
