package com.yimei.cflow.engine.cluster

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.flow.Command

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
