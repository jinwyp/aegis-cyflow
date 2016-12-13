package com.yimei.cflow.core


import java.util.UUID

import akka.actor.{ActorRef, Props, Terminated}
import akka.util.Timeout
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{Command, CommandCreateFlow, CommandRunFlow}
import com.yimei.cflow.core.IdGenerator.{CommandGetId, Id}
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import scala.concurrent.duration._
import akka.pattern._

/**
  * Created by hary on 16/12/1.
  */
object FlowMaster {
  def props(dependOn: Array[String], persist: Boolean = true): Props = Props(new FlowMaster(dependOn, persist))
}

/**
  *
  * @param dependOn dependent modules' names
  * @param persist  use persist actor or not
  */
class FlowMaster(dependOn: Array[String], persist: Boolean = true)
  extends ModuleMaster(module_flow, dependOn)
    with ServicableBehavior {

  def serving: Receive = {

    // create and run flow
    case command@CommandCreateFlow(flowType, guid) =>

      // use IdGenerator to get persistenceId
      // todo check it
      if (false) {
        implicit val ec = context.system.dispatcher
        implicit val timeout = Timeout(3 seconds)
        val fpid = (modules(module_id) ? CommandGetId("flow")).mapTo[Id]
        for (pid <- fpid) {
          val flowId = s"${flowType}-${guid}-${pid}" // 创建flowId
          val child = create(flowId)
          child forward CommandRunFlow(flowId)
        }
      }

      // use UUID to generate persistenceId
      val flowId = s"${flowType}-${guid}-${UUID.randomUUID().toString}" // 创建flowId
    val child = create(flowId)
      child forward CommandRunFlow(flowId)

    // other command
    case command: Command =>
      log.debug(s"get command $command and forward to child!!!!")
      val child = context.child(command.flowId).fold(create(command.flowId))(identity)
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  /**
    * 创建
    *
    * @param flowId
    * @return
    */
  def create(flowId: String): ActorRef = {
    val regex = "(\\w+)-(\\w+-\\w+)-(.*)".r
    val p = flowId match {
      case regex(flowType, guid, persistenceId) =>

        log.info(s"flowId in flowProp is ${flowId}, ${guid}, ${persistenceId}")


        val graph = FlowRegistry.getFlowGraph(flowType)
        if (persist) {
          log.info(s"创建persistent flow..........")
          PersistentFlow.props(graph, flowId, modules, persistenceId, guid)
        } else {
          log.info(s"创建non-persistent flow..........")
          MemoryFlow.props(graph, flowId, modules, guid)
        }
    }

    context.actorOf(p, flowId)
  }

}


