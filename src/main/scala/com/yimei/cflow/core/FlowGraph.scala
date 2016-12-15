package com.yimei.cflow.core

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.core.Flow.{Arrow, Decision, Graph, State}


object FlowGraph {

  case class AutoBuilder( name: String = "",
                          points: Array[String] =Array(),
                          acc: Map[String, (Array[String], Map[String, ActorRef] => Props)] = Map()) {
    def actor(actorName: String) = this.copy(name = actorName)
    def points(pointNames: String*) = this.copy(points = pointNames.toArray)
    def prop(propfun: Map[String, ActorRef] => Props) = {
      val curName = this.name
      val curPoints = this.points
      this.copy(name = "", points = Array(), acc = this.acc + (curName -> (curPoints, propfun)))
    }
    def done = acc
  }

  case class TaskBuilder(name: String = "", acc: Map[String, Array[String]] = Map()) {
    def task(taskName: String) = this.copy(name = taskName)
    def points(pointNames: String*) = {
      val curName = this.name
      this.copy(name = "", acc = this.acc + (curName -> pointNames.toArray))

    }
    def done = this.acc
  }

  def autoBuilder = AutoBuilder()

  def taskBuilder = TaskBuilder()

}


/**
  *
  */
trait FlowGraph {
  /**
    * initial decision point
    * @return
    */
  def getFlowInitial: Decision

  /**
    *
    * @param state
    * @return
    */
  def getFlowGraph(state: State): Graph

  /**
    * flow type
    * @return
    */
  def getFlowType: String

  /**
    *
    */
  def getAutoTask: Map[String, (Array[String], Map[String, ActorRef] => Props)]

  /**
    * 注册用户任务
    */
  def getUserTask: Map[String, Array[String]]

  /**
    * 所有决策点
    */
  def getJudges: Map[String, State => Arrow] = Map()

}




