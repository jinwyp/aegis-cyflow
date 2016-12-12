package com.yimei.cflow.core


import akka.actor.Props
import com.yimei.cflow.integration.{DependentModule, ModuleMaster}
import com.yimei.cflow.config.GlobalConfig._

/**
  * Created by hary on 16/12/1.
  */
object FlowMaster {
  def props(dependOn: Array[String], persist: Boolean = true): Props = Props(new FlowMaster(dependOn, persist))
}

/**
  *
  * @param dependOn   dependent modules' names
  * @param persist    use persist actor or not
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
          PersistentFlow.props(graph, flowId, modules, persistenceId, guid, parties)
        } else {
          log.info(s"创建non-persistent flow..........")
          MemoryFlow.props(graph, flowId, modules, guid, parties)
        }
    }
  }
}


