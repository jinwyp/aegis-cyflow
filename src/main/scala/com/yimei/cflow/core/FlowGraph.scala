package com.yimei.cflow.core

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.FlowRegistry.AutoProperty

//
trait GraphJar {
  val flowType: String
  def flowGraph: FlowGraph = FlowRegistry.getFlowGraph(flowType)
  def getDeciders: Map[String, State => Arrow]
}

abstract class AutoActor(modules: Map[String, ActorRef]) extends Actor with ActorLogging

object FlowGraph {

  case class AutoBuilder(_name: String = "",
                         _points: Array[String] =Array(),
                         _acc: Map[String, AutoProperty] = Map()) {
    def actor(actorName: String) = this.copy(_name = actorName)
    def points(pointNames: Array[String]) = this.copy(_points = pointNames)
    def prop(propfun: Map[String, ActorRef] => Props) = {
      val curName = this._name
      val curPoints = this._points
      this.copy(_name = "", _points = Array(), _acc = this._acc + (curName ->  AutoProperty(curPoints, propfun)))
    }
    def done = _acc
  }

  case class TaskBuilder(_name: String = "", _acc: Map[String, Array[String]] = Map()) {
    def task(taskName: String) = this.copy(_name = taskName)
    def points(pointNames: String*) = {
      val curName = this._name
      this.copy(_name = "", _acc = this._acc + (curName -> pointNames.toArray))

    }
    def done = this._acc
  }

  case class DeciderBuilder(_name: String = "", _acc: Map[String, State => Arrow] = Map()) {
    def decision(name: String) = this.copy(_name = name)
    def is(decider: State => Arrow): DeciderBuilder = {
      this.copy(_acc = this._acc + (this._name -> decider))
    }
    def done = this._acc
  }

  def autoBuilder = AutoBuilder()

  def taskBuilder = TaskBuilder()

  def deciderBuilder = DeciderBuilder()

}


/**
  *
  */
trait FlowGraph {
  /**
    * initial decision point
    * @return
    */
  def getFlowInitial:String

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
  // def getAutoTask: Map[String, (Array[String], Map[String, ActorRef] => Props)]
  def getAutoTask: Map[String, AutoProperty]

  /**
    * 注册用户任务
    */
  def getUserTask: Map[String, Array[String]]

  /**
    *
    */
  def getEdges: Map[String, Edge]

  /**
    * 所有决策点
    */
  def getDeciders: Map[String, State => Arrow]

}




