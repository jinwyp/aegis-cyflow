package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging}
import com.yimei.cflow.core.FlowRegistry._

/**
  * some common facilities
  */
abstract class AbstractFlow extends Actor with ActorLogging {

  import Flow._

  // 数据点名称 -> 数据点值
  var state: State

  val graph: FlowGraph

  //
  def genGraph(state: State): Graph

  //
  def updateState(ev: Event) = {
    ev match {
//      case Hijacked(updatePoints) => updateDecision match {
//        case Some(v) =>
//          state = state.copy(points = state.points ++ updatePoints)
//        case None =>
//          state = state.copy(points = state.points ++ updatePoints)
//      }
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case PointsUpdated(map) => state = state.copy(points = state.points ++ map)

        // 边完成
      case EdgeCompleted(name) =>
        state = state.copy(
          edges = state.edges - name,
          histories = name +: state.histories
        )

      case DecisionUpdated(name, arrows) =>

        // 将当前点的入边所负责的数据点设置为已使用
        var newPoints = state.points
        graph.inEdges(name)
          .map(graph.edges(_))
          .map(_.getAllDataPointsName(state))
          .foldLeft(Seq[String]())((acc, elem) =>  acc ++ elem)
          .foreach { ap =>
            newPoints = newPoints + (ap -> newPoints(ap).copy(used = true))
          }

        state = state.copy(
          edges = state.edges ++ arrows.map(_.edge).map(e => (e.get -> true)).toMap,
          points = newPoints
        )

        log.debug("new status: {}", state)
    }
  }

  def logState(mark: String = ""): Unit = {
    log.info(s"<$mark>current state: { ${state.edges} [${state.histories.mkString(",")}]} + {${state.points.map(_._1).mkString(",")}} + {${state.guid}}")
  }

  def commonBehavior: Receive = {
    // return the whole graph = state + graph
    case query: CommandFlowGraph =>
      sender() ! genGraph(state)

    // only return the state
    case query: CommandFlowState =>
      sender() ! state

    case shutdown: CommandShutdown =>
      log.info("received CommandShutdown")
      context.stop(self)
  }
}
