package com.yimei.cflow.core


import akka.actor.{ActorRef, Props}
import com.yimei.cflow.integration.ModuleMaster

object EngineMaster {
  def props(name: String, dependOn: Array[String]): Props = Props(new EngineMaster(name, dependOn))
}

/**
  * Created by hary on 16/12/1.
  */
class EngineMaster(name: String, dependOn: Array[String]) extends ModuleMaster(name, dependOn) with FlowMasterBehavior {

  override def getModules(): Map[String, ActorRef] = modules

  /**
    *
    * @param flowId   流程id
    * @param modules  依赖的模块
    * @param userId   用户id
    * @param parties  参与方用户
    * @return
    */
  override def flowProp(graph: FlowGraph,
                        flowId: String,
                        modules: Map[String, ActorRef],
                        userId: String,
                        parties: Map[String, String] = Map()): Props =
    Engine.props(graph, flowId, getModules(), userId, parties)
}


