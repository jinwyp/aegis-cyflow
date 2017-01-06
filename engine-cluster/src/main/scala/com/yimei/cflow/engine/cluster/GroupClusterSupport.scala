package com.yimei.cflow.engine.cluster

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.api.models.group.Command

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
