package com.yimei.cflow.auto

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.core.Flow.State
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.auto.AutoMaster.{GetAutoData}
import com.yimei.cflow.integration.{ ModuleMaster, ServicableBehavior}

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
      autoMaster ! GetAutoData(state.flowId, actorName)
    }
  }

  /**
    *
    * @param flowId   flowId
    * @param actorName actorName
    */
  case class GetAutoData(flowId: String, actorName: String)


  def props(dependOn: Array[String]) = Props(new AutoMaster(dependOn))
}

trait AutoMasterBehavior extends Actor with ServicableBehavior with ActorLogging {

  var actors = Map[String, ActorRef]()

  override def serving: Receive = {
    // 将采集请求转发
    case  get @GetAutoData(flowId, actorName)  =>
      log.debug(s"forward ${get} to ${actors(actorName)} ")
      actors(actorName) forward  get
  }
}

class AutoMaster(dependOn: Array[String]) extends ModuleMaster(module_auto, dependOn) with AutoMasterBehavior {

  import AutoActors.actorProps;

  override def initHook(): Unit = {
    log.debug("DataMaster initHook now!!!!")
    actors = actorProps(modules).map(entry => (entry._1, context.actorOf(entry._2, entry._1)))
  }
}

