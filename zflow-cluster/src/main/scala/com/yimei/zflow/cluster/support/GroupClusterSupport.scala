package com.yimei.zflow.cluster.support

import akka.actor.{Actor, ActorInitializationException, DeathPactException, OneForOneStrategy, Props, SupervisorStrategy}
import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.group.Command
import com.yimei.cflow.engine.group.PersistentGroup

/**
  * Created by hary on 16/12/16.
  */
trait GroupClusterSupport {
  // for cluster
  val groupExtractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.ggid, cmd)
  }

  val groupNumberOfShards = 100

  val groupExtractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.ggid.hashCode % groupNumberOfShards).toString
  }

  val groupShardName = "group"
}

class GroupSupervisor extends Actor {
  val group = context.actorOf(Props[PersistentGroup], "theGroup")

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalArgumentException ⇒ SupervisorStrategy.Resume
    case _: ActorInitializationException ⇒ SupervisorStrategy.Stop
    case _: DeathPactException ⇒ SupervisorStrategy.Stop
    case _: Exception ⇒ SupervisorStrategy.Restart
  }

  def receive = {
    case msg ⇒ group forward msg
  }
}

