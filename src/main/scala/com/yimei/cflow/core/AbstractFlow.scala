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
  def queryStatus(state: State): String

  //
  def updateState(ev: Event) = {
    ev match {
      case UserUpdated(newUserId) => state = state.copy(userId = newUserId)
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case PointsUpdated(map) => state = state.copy(points = state.points ++ map)
      case DecisionUpdated(d) => state = state.copy(decision = d, histories = state.decision.toString :: state.histories)
    }
  }

  def logState(mark: String = ""): Unit = {
    log.info(s"<$mark>current state: {${state.decision} [${state.histories.mkString(",")}]} + {${state.points.map(_._1).mkString(",")}} + {${state.userId}}")
  }
}
