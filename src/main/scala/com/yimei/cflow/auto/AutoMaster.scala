package com.yimei.cflow.auto

import akka.actor.{ActorRef, Props}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.graph.ying.YingConfig._
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}

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
  def fetch(actorName: String, state: State, autoMaster: ActorRef, refetchIfExists: Boolean = false) = {
    if ( refetchIfExists ||
      dataPointMap(actorName).filter(!state.points.contains(_)).length > 0
    ) {
      // 给autoMaster发送获取数据请求
      autoMaster ! CommandAutoTask(state.flowId, actorName)
    }
  }



  /**
    *
    * @param flowId   flowId
    * @param actorName actorName
    */
  case class CommandAutoTask(flowId: String, actorName: String)


  def props(dependOn: Array[String]) = Props(new AutoMaster(dependOn))
}

class AutoMaster(dependOn: Array[String]) extends ModuleMaster(module_auto, dependOn) with ServicableBehavior {

  import AutoRegistry._

  // all child auto actors
  var actors = Map[String, ActorRef]()

  // servicable behavior
  override def serving: Receive = {
    case  get @CommandAutoTask(flowId, actorName)  =>
      log.info(s"forward ${get} to ${actors(actorName)} ")
      actors(actorName) forward  get
  }

  // create all child actors
  override def initHook(): Unit = {
    log.debug("DataMaster initHook now!!!!")
    for (elem <- propMap) {
      println(s"begin create AutoActor ${elem._1}....")
      actors = actors + (elem._1 -> context.actorOf(elem._2(modules), elem._1))
    }

    println(s"all AutoActors are $actors")
  }
}

