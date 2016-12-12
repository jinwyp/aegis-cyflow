package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging}

/**
  * some common facilities
  */
abstract class AbstractFlow extends Actor with ActorLogging {

  import Flow._

  // 数据点名称 -> 数据点值
  var state: State

  //
  def queryFlow(state: State): Graph

  //
  def updateState(ev: Event) = {
    ev match {
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case PointsUpdated(map) => state = state.copy(points = state.points ++ map)
      case DecisionUpdated(arrow) => state = state.copy(
        decision = arrow.end,
        edge = arrow.edge,
        histories = arrow :: state.histories
      )
      case PartiesUpdated(ps) =>
        log.info(s"begin update with ${ps}")
        val k: Map[String, String] = ps
        state = state.copy( parties =  state.parties ++ ps)
    }
  }

  def logState(mark: String = ""): Unit = {
    log.info(s"<$mark>current state: { ${state.edge} -> ${state.decision} [${state.histories.mkString(",")}]} + {${state.points.map(_._1).mkString(",")}} + {${state.guid}}")
  }

  def commonBehavior: Receive = {
    case query: CommandQueryFlow =>
      sender() ! queryFlow(state)

    case shutdown: CommandShutdown =>
      log.info("received CommandShutdown")
      context.stop(self)
  }
}
