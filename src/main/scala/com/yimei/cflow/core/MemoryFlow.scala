package com.yimei.cflow.core

import java.util.{Date, UUID}

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.integration.DependentModule

object MemoryFlow {
  /**
    *
    * @param graph       flow graph
    * @param flowId      flow Id  (flowType-userType*userId-persistenceId)
    * @param modules     injected dependent modules
    * @param guid         global userId (userType + userId)
    * @return
    */
  def props(graph: FlowGraph, flowId: String, modules: Map[String, ActorRef],guid: String) = {
    Props(new MemoryFlow(graph, flowId, modules, guid))
  }

}

/**
  *
  * @param graph        flow graph
  * @param flowId       flow id(flowType-userType-userId-persistenceId)
  * @param dependOn     injected modules
  * @param guid         global userId (userType + userId)
  */
class MemoryFlow(graph: FlowGraph, flowId: String, dependOn: Map[String, ActorRef], guid: String)
  extends AbstractFlow
    with DependentModule {

  import Flow._

  override var state: State = State(flowId, guid, Map[String, DataPoint](), graph.getFlowInitial, Some(EdgeStart), Nil)

  override def queryFlow(state: State): Graph = graph.getFlowGraph(state)

  override def modules: Map[String, ActorRef] = dependOn

  override def receive: Receive = commonBehavior orElse serving

  // servicable
  val serving: Receive = {
    case cmd@CommandRunFlow(flowId) =>
      log.info(s"收到${cmd}")
      sender() ! RunFlowSuccess(flowId)
      makeDecision() // 注意顺序

    case cmd: CommandPoint =>
      log.info(s"received ${cmd.name}")
      processCommandPoint(cmd)

    case cmd: CommandPoints =>
      log.info(s"received ${cmd.points.map(_._1).mkString("[", ",", "]")}")
      processCommandPoints(cmd)

    case cmd: CommandUpdatePoints =>
      val uuid = UUID.randomUUID().toString
      val points: Map[String, DataPoint] = cmd.points.map { entry =>
        entry._1 -> DataPoint(entry._2, None, None, uuid, new Date())
      }
      updateState(PointsUpdated(points))
  }

  //
  protected def processCommandPoint(cmd: CommandPoint) = {
    updateState(PointUpdated(cmd.name, cmd.point))
    makeDecision()
  }

  protected def processCommandPoints(cmdpoints: CommandPoints) = {
    updateState(PointsUpdated(cmdpoints.points))
    makeDecision()
  }

  private def makeDecision() {
    val curDecision = state.decision

    curDecision.run(state) match {
      case arrow @ Arrow(j, Some(e)) =>
        updateState(DecisionUpdated(arrow))
        log.info(s"schedule edge = ${e}")

        // 继续执行图!!!
        if( e.check(state)) {
          makeDecision()
        } else {
          e.schedule(state, modules)
        }

      case Arrow(FlowTodo, None) =>
        logState("FlowTodo")

      case Arrow(FlowSuccess, None) =>
        updateState(DecisionUpdated(Arrow(FlowSuccess, None)))
        logState("FlowSuccess")

      case Arrow(FlowFail, None) =>
        updateState(DecisionUpdated(Arrow(FlowFail, None)))
        logState("FlowFail")
    }
  }
}