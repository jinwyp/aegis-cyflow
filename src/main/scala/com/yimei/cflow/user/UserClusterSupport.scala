package com.yimei.cflow.user

import akka.cluster.sharding.ShardRegion
import com.yimei.cflow.user.User.Command

/**
  * Created by hary on 16/12/16.
  */
trait UserClusterSupport {
  // for cluster
  val extractEntityId: ShardRegion.ExtractEntityId = {
    case cmd: Command => (cmd.guid, cmd)
  }

  val numberOfShards = 100

  val extractShardId: ShardRegion.ExtractShardId = {
    case cmd: Command => (cmd.guid.hashCode % numberOfShards).toString
  }
}
