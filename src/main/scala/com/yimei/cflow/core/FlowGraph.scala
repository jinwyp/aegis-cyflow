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
  val persistent: Boolean
  val points: Map[String, String]
  val edges: Map[String, Edge]
  val vertices: Map[String, String]
  val inEdges: Map[String, Array[String]] = Map()
  val flowType: String
  val userTasks: Map[String, TaskInfo]
  val autoTasks: Map[String, TaskInfo]
  val pointEdges: Map[String, String]

  val blueprint: Graph = Graph(edges, vertices, None, points, userTasks, autoTasks)

  def graph(state: State): Graph = Graph(edges, vertices, Some(state), points, userTasks, autoTasks)

  // todo 王琦优化!!!!
  protected def pointEdgesImpl: Map[String, String] = {
    def process(name: String, e: Edge) = {

      val userPointMap = e.userTasks.map { (ut: String) =>
        userTasks(ut).points.map(pt =>
          (pt -> name)
        ).toMap
      }.foldLeft(Map[String, String]())((acc, elem) => acc ++ elem)

      val autoPointMap = e.autoTasks.map { (ut: String) =>
        autoTasks(ut).points.map(pt =>
          (pt -> name)
        ).toMap
      }.foldLeft(Map[String, String]())((acc, elem) => acc ++ elem)

      val partUPointMap = (for {
        put <- e.partUTasks
        task <- put.tasks
        pt <- userTasks(task).points
      } yield (pt -> name)).toMap

      val partGPointMap = (for {
        put <- e.partGTasks
        task <- put.tasks
        pt <- userTasks(task).points
      } yield (pt -> name)).toMap

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

  val deciders: Map[String, State => Seq[Arrow]] = {
    this.getClass.getMethods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[State] &&
        m.getReturnType == classOf[Seq[Arrow]]
    }.map { am =>
      val behavior: State => Seq[Arrow] = (state: State) =>
        am.invoke(this, state).asInstanceOf[Seq[Arrow]]
      (am.getName -> behavior)
    }.toMap  ++ Map( "success" -> null, "fail" -> null)
  }

  val moduleJar: AnyRef = this
}




