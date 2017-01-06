package com.yimei.cflow.engine.cluster

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.user.Command

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
