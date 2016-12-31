package com.yimei.cflow.engine.auto

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.api.models.flow.State
import com.yimei.cflow.api.services.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.FlowRegistry
import com.yimei.cflow.engine.auto.AutoMaster.CommandAutoTask

/**
  * Created by hary on 16/12/3.
  */
object AutoMaster {

  /**
    * @param actorName
    * @param state           current state of the flow
    * @param autoMaster      auto service master
    * @param refetchIfExists should refetch if exists
    */
  def fetch(flowType: String, actorName: String, state: State, autoMaster: ActorRef, refetchIfExists: Boolean = false) = {
    if (refetchIfExists ||
      FlowRegistry.registries(flowType).autoTasks(actorName).points.filter(!state.points.filter(t => (!t._2.used)).contains(_)).length > 0
    ) {
      autoMaster ! CommandAutoTask(state, flowType, actorName)
    }
  }

  /**
    *
    * @param state     State
    * @param actorName actorName
    */
  case class CommandAutoTask(state: State, flowType: String, actorName: String)

  def props(dependOn: Array[String]) = Props(new AutoMaster(dependOn))

}

class AutoMaster(dependOn: Array[String]) extends ModuleMaster(module_auto, dependOn) with ServicableBehavior {

  // all child auto actors flowType -> actorName -> actorRef
  var actors = Map[String, Map[String, ActorRef]]()

  // servicable behavior
  override def serving: Receive = {
    case get@CommandAutoTask(flowId, flowType, actorName) =>
      log.debug(s"forward ${get} to ${actors(flowType)(actorName)} ")
      actors(flowType)(actorName) forward get
  }

  // create all child actors
  override def initHook(): Unit = {
    startActors
  }

  def startActors() = {

    FlowRegistry.registries.foreach { e =>
      val flowType = e._1
      val graph = e._2
      val jar = graph.moduleJar

      for (be <- graph.autoMethods) {

        val actor = context.actorOf(Props(new AutoActor(be._1, modules, be._2)), s"${flowType}.${be._1}")
        if (actors.contains(flowType)) {
          val entry = actors(flowType) + (be._1 -> actor)
          actors = actors + (flowType -> entry)
        } else {
          actors = actors + (flowType -> Map(be._1 -> actor))
        }
      }
    }
  }
}

