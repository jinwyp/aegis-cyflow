package com.yimei.cflow.core

import java.lang.reflect.Method
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow._

import scala.concurrent.Future

class AutoActor(
                 name: String,
                 modules: Map[String, ActorRef],
                 auto: CommandAutoTask => Future[Map[String, String]]
               )
  extends Actor
    with ActorLogging {

  import scala.concurrent.ExecutionContext
  import ExecutionContext.Implicits.global

  override def receive: Receive = {
    case task: CommandAutoTask =>
      auto(task).map { values =>
        modules(module_flow) ! CommandPoints(
          task.state.flowId,
          values.map { entry =>
            ((entry._1) -> DataPoint(entry._2, None, Some(name), UUID.randomUUID().toString, 10, false))
          }
        )
      }
  }
}


object FlowGraph {

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

  def taskBuilder = TaskBuilder()

  def deciderBuilder = DeciderBuilder()

}


/**
  *
  */
trait FlowGraph {
  val flowInitial: String
  val timeout: Long

  def graph(state: State): Graph

  val blueprint: Graph = graph(null)
  val edges: Map[String, Edge]
  val inEdges: Map[String, Array[String]] = Map()
  val flowType: String
  val userTasks: Map[String, Array[String]]
  val autoTasks: Map[String, Array[String]]
  val pointEdges: Map[String, String]

  // todo 王琦优化!!!!
  protected def  pointEdgesImpl: Map[String, String] = {
    def process(name: String, e: Edge) = {

      val userPointMap = e.userTasks.map { (ut: String) =>
        userTasks(ut).map( pt =>
          (pt -> name)
        ).toMap
      }.foldLeft(Map[String, String]())((acc, elem) => acc ++ elem)

      val autoPointMap = e.autoTasks.map { (ut: String) =>
        autoTasks(ut).map( pt =>
          (pt -> name)
        ).toMap
      }.foldLeft(Map[String, String]())((acc, elem) => acc ++ elem)

      val partGPointMap = e.partGTasks.map(_.ggidKey).map{ _ -> name}.toMap

      val partUPointMap = e.partUTasks.map(_.guidKey).map{ _ -> name}.toMap

      autoPointMap ++ userPointMap ++ partUPointMap ++ partGPointMap
    }

    var ret: Map[String, String] = Map()
    for ((name, ed) <- edges) {
      ret = ret ++ process(name, ed)
    }

    ret
  }

  val autoMethods: Map[String, Method] = {
    this.getClass.getMethods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[CommandAutoTask] &&
        m.getReturnType == classOf[Future[Map[String, String]]]
    }.map { am =>
      (am.getName -> am)
    }.toMap
  }

  val deciders: Map[String, State => Arrow] = {
    this.getClass.getMethods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[State] &&
        m.getReturnType == classOf[Arrow]
    }.map { am =>

      val behavior: State => Arrow = (state: State) =>
        am.invoke(this, state).asInstanceOf[Arrow]
      (am.getName -> behavior)
    }.toMap
  }

  val moduleJar: AnyRef = this
}




