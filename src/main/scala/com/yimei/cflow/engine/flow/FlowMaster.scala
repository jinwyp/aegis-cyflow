package com.yimei.cflow.engine.flow

import java.util.UUID

import akka.actor.{ActorRef, Props, Terminated}
import akka.util.Timeout
import com.yimei.cflow.api.models.flow.{Command, CommandCreateFlow, CommandRunFlow}
import com.yimei.cflow.api.services.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.FlowRegistry
import com.yimei.cflow.id.IdBufferable

import scala.concurrent.duration._

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
    with ServicableBehavior
    with IdBufferable {

  // IdBufferable need this
  override val bufferSize: Int = 100
  override val bufferKey: String = "flow"

  override def myIdGenerator = modules(module_id)

  implicit val myEc = context.system.dispatcher
  implicit val myTimeout = Timeout(3 seconds)

  def serving: Receive = {

    // create and run flow
    case command@CommandCreateFlow(flowType, guid, initData) =>

      if (true) {
        val pid = nextId
        val flowId = s"${flowType}!${guid}!${pid}" // 创建flowId
        val child = create(flowId, initData)
        child forward CommandRunFlow(flowId)

      } else {
        // use UUID to generate persistenceId
        val flowId = s"${flowType}!${guid}!${UUID.randomUUID().toString}" // 创建flowId
        val child = create(flowId, initData)
        child forward CommandRunFlow(flowId)
      }

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
  def create(flowId: String, initData: Map[String, String] = Map()): ActorRef = {
    val regex = "([^!]+)!([^!]+![^!]+)!(.*)".r
    val p = flowId match {
      case regex(flowType, guid, persistenceId) =>

        log.info(s"flowId in flowProp is ${flowId}, ${guid}, ${persistenceId}")


        val graph = FlowRegistry.flowGraph(flowType)
        if (persist) {
          log.info(s"创建persistent flow..........")
          PersistentFlow.props(graph, flowId, modules, persistenceId, guid, initData)
        } else {
          log.info(s"创建non-persistent flow..........")
          MemoryFlow.props(graph, flowId, modules, guid, initData)
        }
    }

    context.actorOf(p, flowId)
  }

}


