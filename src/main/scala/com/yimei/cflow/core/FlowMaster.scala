package com.yimei.cflow.core


import akka.actor.Props
import com.yimei.cflow.integration.{DependentModule, ModuleMaster}
import com.yimei.cflow.config.GlobalConfig._

object FlowMaster {
  def props(dependOn: Array[String], persist: Boolean = true): Props = Props(new FlowMaster(dependOn, persist))
}

/**
  * Created by hary on 16/12/1.
  */
class FlowMaster(dependOn: Array[String], persist: Boolean = true) extends ModuleMaster(module_flow, dependOn)
  with FlowMasterBehavior
  with DependentModule {

  /**
    *
    * @param flowId  流程id
    * @param parties 参与方用户
    * @return
    */
  override def flowProp(flowId: String, parties: Map[String, String] = Map()): Props = {
    val regex = "(\\w+)-(\\w+-\\w+)-(.*)".r
    flowId match {
      case regex(flowType, guid, persistenceId) =>

        log.info(s"flowId in flowProp is ${flowId}, ${guid}, ${persistenceId}")


        val graph = FlowRegistry.getFlowGraph(flowType)
        if (persist) {
          log.info(s"创建persistent flow..........")
          PersistentEngine.props(graph, flowId, persistenceId, modules, guid, parties)
        } else {
          log.info(s"创建non-persistent flow..........")
          Engine.props(graph, flowId, modules, guid, parties)
        }
    }
  }
}


