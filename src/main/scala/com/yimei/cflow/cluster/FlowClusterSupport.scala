package com.yimei.cflow.cluster

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.flow.Command

/**
  * Created by hary on 16/12/16.
  */
object FlowClusterSupport {
  // for cluster
  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.flowId, cmd)
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.flowId.hashCode % numberOfShards).toString
  }

  val shardName = "flow"
}