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

  //
  def genGraph(state: State): Graph

  //
  def updateState(ev: Event) = {
    ev match {
      case Hijacked(updatePoints, updateDecision) => updateDecision match {
        case Some(v) =>
          state = state.copy(points = state.points ++ updatePoints, decision = v)
        case None =>
          state = state.copy(points = state.points ++ updatePoints)
      }
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case PointsUpdated(map) => state = state.copy(points = state.points ++ map)
      case DecisionUpdated(arrow) =>
        //把当前边里不能重用的点设置为used = true
        val newEdge = arrow.edge match {
          case Some(e) => Some(edges(state.flowType)(e))
          case a => None
        }
        state.edge match {

          // todo 王琦 optimize
          case Some(e) =>
            val allPoints = e.getAllDataPointsName(state) // 所有需要设置为used的数据点
          var newPoints = state.points
            allPoints.foreach(ap => newPoints = newPoints + (ap -> newPoints(ap).copy(used = true))) // for side effect of newPoints
            state = state.copy(
              decision = arrow.end,
              edge = newEdge,
              histories = arrow +: state.histories,
              points = newPoints
            )

          case a =>
            state = state.copy(
              decision = arrow.end,
              edge = newEdge,
              histories = arrow +: state.histories
            )
        }
        log.debug("new status: {}", state)
    }
  }

  def logState(mark: String = ""): Unit = {
    log.info(s"<$mark>current state: { ${state.edge} -> ${state.decision} [${state.histories.mkString(",")}]} + {${state.points.map(_._1).mkString(",")}} + {${state.guid}}")
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
