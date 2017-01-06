package com.yimei.zflow.cluster.support

import akka.actor.{Actor, ActorInitializationException, DeathPactException, OneForOneStrategy, Props, SupervisorStrategy}
import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.user.Command
import com.yimei.cflow.engine.user.PersistentUser

/**
  * Created by hary on 16/12/16.
  */
trait UserClusterSupport {
  // for cluster
  val userExtractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.guid, cmd)
  }

  val userNumberOfShards = 100

  val userShardName = "user"

  val userExtractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.guid.hashCode % userNumberOfShards).toString
  }
}

class UserSupervisor extends Actor {
  val user = context.actorOf(Props[PersistentUser], "theUser")

  override val supervisorStrategy = OneForOneStrategy() {
    case _: IllegalArgumentException     ⇒ SupervisorStrategy.Resume
    case _: ActorInitializationException ⇒ SupervisorStrategy.Stop
    case _: DeathPactException           ⇒ SupervisorStrategy.Stop
    case _: Exception                    ⇒ SupervisorStrategy.Restart
  }

  def receive = {
    case msg ⇒ user forward msg
  }
}

