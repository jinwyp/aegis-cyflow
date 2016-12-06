package com.yimei.cflow.core


import akka.actor.Props
import com.yimei.cflow.integration.{DependentModule, ModuleMaster}

object FlowMaster {
  def props(name: String, dependOn: Array[String], persist: Boolean = true): Props = Props(new FlowMaster(name, dependOn, persist))
}

/**
  * Created by hary on 16/12/1.
  */
class FlowMaster(name: String, dependOn: Array[String], persist: Boolean = true) extends ModuleMaster(name, dependOn)
  with FlowMasterBehavior
  with DependentModule {

  /**
    *
    * @param flowId  流程id
    * @param userId  用户id
    * @param parties 参与方用户
    * @return
    */
  override def flowProp(graph: FlowGraph,
                        flowId: String,
                        userId: String,
                        parties: Map[String, String] = Map()): Props =
    if (persist)
      Engine.props(graph, flowId, modules, userId, parties)
    else
      PersistentEngine.props(graph, flowId, modules, userId, parties)
}


