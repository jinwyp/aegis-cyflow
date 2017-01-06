package com.yimei.cflow.engine.cluster

import akka.actor.{ActorIdentity, ActorPath, ActorSystem, Identify, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern._
import akka.persistence.journal.leveldb.{SharedLeveldbJournal, SharedLeveldbStore}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.yimei.cflow.engine.graph.FlowGraph
import scala.concurrent.duration._

/**
  * Created by hary on 16/12/16.
  */
object FlowClusterApp extends App
with FlowClusterSupport
with GroupClusterSupport
with UserClusterSupport {

  def startup(port: Int, graph: FlowGraph): Unit = {
    // Override the configuration of the port
    val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
      withFallback(ConfigFactory.load())

    // Create an Akka system
    val system = ActorSystem("ClusterSystem", config)

    startupSharedJournal(system, startStore = (port == 2551), path =
      ActorPath.fromString("akka.tcp://ClusterSystem@127.0.0.1:2551/user/store"))

    // 流程
    val flowRegion = ClusterSharding(system).start(
      typeName = flowShardName,
      entityProps = null, // PersistentFlow.props(),
      settings = ClusterShardingSettings(system),
      extractEntityId = flowExtractEntityId,
      extractShardId = flowExtractShardId)

    // 组管理
    val groupRegion = ClusterSharding(system).start(
      typeName = groupShardName,
      entityProps = null,
      settings = ClusterShardingSettings(system),
      extractEntityId = groupExtractEntityId,
      extractShardId = groupExtractShardId)

    // 用户
    val userRegion = ClusterSharding(system).start(
      typeName = userShardName,
      entityProps = null,
      settings = ClusterShardingSettings(system),
      extractEntityId = userExtractEntityId,
      extractShardId = userExtractShardId)

  }

  def startupSharedJournal(system: ActorSystem, startStore: Boolean, path: ActorPath): Unit = {
    // Start the shared journal one one node (don't crash this SPOF)
    // This will not be needed with a distributed journal
    if (startStore)
      system.actorOf(Props[SharedLeveldbStore], "store")
    // register the shared journal
    import system.dispatcher
    implicit val timeout = Timeout(15.seconds)
    val f = (system.actorSelection(path) ? Identify(None))
    f.onSuccess {
      case ActorIdentity(_, Some(ref)) => SharedLeveldbJournal.setStore(ref, system)
      case _ =>
        system.log.error("Shared journal not started at {}", path)
        system.terminate()
    }
    f.onFailure {
      case _ =>
        system.log.error("Lookup of shared journal at {} timed out", path)
        system.terminate()
    }
  }

}
