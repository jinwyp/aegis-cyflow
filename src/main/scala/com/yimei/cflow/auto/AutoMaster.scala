package com.yimei.cflow.auto

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.core.FlowRegistry
import com.yimei.cflow.core.FlowRegistry.AutoProperty

/**
  * Created by hary on 16/12/3.
  */
object AutoMaster {

  /**
    * @param actorName
    * @param state               current state of the flow
    * @param autoMaster          auto service master
    * @param refetchIfExists     should refetch if exists
    */
  def fetch(flowType:String, actorName: String, state: State, autoMaster: ActorRef, refetchIfExists: Boolean = false) = {
    if ( refetchIfExists ||
      // FlowRegistry.autoTask(flowType)(actorName)._1.filter(!state.points.filter(t=>(!t._2.used)).contains(_)).length > 0
      FlowRegistry.autoTask(flowType)(actorName).points.filter(!state.points.filter(t=>(!t._2.used)).contains(_)).length > 0
    ) {
      autoMaster ! CommandAutoTask(state.flowId, flowType, actorName)
    }
  }

  /**
    *
    * @param flowId   flowId
    * @param actorName actorName
    */
  case class CommandAutoTask(flowId: String, flowType:String, actorName: String)

  def props(dependOn: Array[String]) = Props(new AutoMaster(dependOn))

}

class AutoMaster(dependOn: Array[String]) extends ModuleMaster(module_auto, dependOn) with ServicableBehavior {

  // all child auto actors flowType -> actorName -> actorRef
  var actors = Map[String ,Map[String, ActorRef]]()

  // servicable behavior
  override def serving: Receive = {
    case  get @CommandAutoTask(flowId, flowType ,actorName)  =>
      log.info(s"forward ${get} to ${actors(flowType)(actorName)} ")
      actors(flowType)(actorName) forward  get
  }

//  // create all child actors
//  override def initHook(): Unit = {
//    log.debug("DataMaster initHook now!!!!")
//    for (elem: (String, Map[String, (Array[String], (Map[String, ActorRef]) => Props)]) <- FlowRegistry.autoTask) {
//      log.debug(s"begin create flowType ${elem._1}....")
//      actors = actors + (elem._1 -> elem._2.foldLeft(Map[String,ActorRef]())((t, e)=>t + (e._1 -> context.actorOf(e._2._2(modules),e._1))) )
//    }
//    log.debug(s"all AutoActors are $actors")
//  }

  // create all child actors
  override def initHook(): Unit = {
    log.debug("DataMaster initHook now!!!!")

    for ((flowType, autoTasks: Map[String, AutoProperty]) <- FlowRegistry.autoTask) {
      log.debug(s"begin create flowType ${flowType}....")
      for ((actorName, autoProp) <- autoTasks) {
        val actor = context.actorOf(autoProp.prop(modules), s"${flowType}.${actorName}")
        if (actors.contains(flowType)) {
          val entry = actors(flowType) + (actorName -> actor)
          actors = actors + (flowType -> entry)
        } else {
          actors = actors + (flowType -> Map(actorName -> actor))
        }
      }
    }

    // actors = actors + (elem._1 -> elem._2.foldLeft(Map[String,ActorRef]())((t, e)=>t + (e._1 -> context.actorOf(e._2._2(modules),e._1))) )
    log.debug(s"all AutoActors are $actors")
  }

}

