package com.yimei.cflow.cluster

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.group.Command

/**
  * Created by hary on 16/12/16.
  */
object GroupClusterSupport {
  // for cluster
  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.ggid, cmd)
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.ggid.hashCode % numberOfShards).toString
  }

  val shardName = "group"
}
