package com.yimei.cflow.user

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.user.User.Command

/**
  * Created by hary on 16/12/16.
  */
object UserClusterSupport {
  // for cluster
  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.guid, cmd)
  }

  val numberOfShards = 100

  val shardName = "user"

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.guid.hashCode % numberOfShards).toString
  }
}
