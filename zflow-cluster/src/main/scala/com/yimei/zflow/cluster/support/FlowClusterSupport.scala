package com.yimei.zflow.cluster.support

import akka.actor.{Actor, ActorInitializationException, DeathPactException, OneForOneStrategy, Props, SupervisorStrategy}
import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.flow.Command
import com.yimei.cflow.engine.flow.PersistentFlow

/**
  * Created by hary on 16/12/16.
  */
trait FlowClusterSupport {
  // for cluster
  val flowExtractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.flowId, cmd)
  }

  val flowNumberOfShards = 100

  val flowExtractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.flowId.hashCode % flowNumberOfShards).toString
  }

  val flowShardName = "flow"
}

class FlowSupervisor extends Actor {
  val flow = context.actorOf(Props[PersistentFlow], "theFlow")

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalArgumentException ⇒ SupervisorStrategy.Resume
    case _: ActorInitializationException ⇒ SupervisorStrategy.Stop
    case _: DeathPactException ⇒ SupervisorStrategy.Stop
    case _: Exception ⇒ SupervisorStrategy.Restart
  }

  def receive = {
    case msg ⇒ flow forward msg
  }
}