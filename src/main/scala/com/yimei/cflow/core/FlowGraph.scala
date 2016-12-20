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
  /**
    * initial decision point
    *
    * @return
    */
  val flowInitial: String

  val timeout: Long

  /**
    *
    * @param state
    * @return
    */
  def graph(state: State): Graph


  /**
    *
    */
  val blueprint: Graph = graph(null)

  /**
    *
    * @return
    */
  val inEdges: Map[String, Array[String]] = Map()

  /**
    * flow type
    *
    * @return
    */
  val flowType: String

  /**
    * 注册用户任务
    */
  val userTasks: Map[String, Array[String]]

  /**
    *
    * @return
    */
  val autoTasks: Map[String, Array[String]]

  /**
    *
    */
  val edges: Map[String, Edge]


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

  /**
    *
    * @return
    */
  val deciders: Map[String, State => Arrow] = {
    this.getClass.getMethods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[State] &&
        m.getReturnType == classOf[Arrow]
    }.map { am =>

      val behavior: State => Arrow  = (state: State)  =>
        am.invoke(this, state).asInstanceOf[Arrow]
      (am.getName -> behavior)
    }.toMap
  }

  /**
    *
    * @return
    */
  val moduleJar: AnyRef = this
}




