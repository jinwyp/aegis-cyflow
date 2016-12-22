package com.yimei.cflow.driver

import akka.actor.{ActorIdentity, ActorPath, ActorSystem, Identify, Props}
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.pattern._
import akka.persistence.journal.leveldb.{SharedLeveldbJournal, SharedLeveldbStore}
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import com.yimei.cflow.core.{FlowClusterSupport, FlowGraph, PersistentFlow}
import com.yimei.cflow.group.GroupClusterSupport
//import com.yimei.cflow.group.{Group, GroupClusterSupport}
import com.yimei.cflow.api.models.group._
import com.yimei.cflow.user.UserClusterSupport

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/16.
  */
object FlowClusterApp extends App {

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
      typeName = FlowClusterSupport.shardName,
      entityProps = null, // PersistentFlow.props(),
      settings = ClusterShardingSettings(system),
      extractEntityId = FlowClusterSupport.extractEntityId,
      extractShardId = FlowClusterSupport.extractShardId)

    // 组管理
    val groupRegion = ClusterSharding(system).start(
      typeName = GroupClusterSupport.shardName,
      entityProps = Group.props(),
      settings = ClusterShardingSettings(system),
      extractEntityId = GroupClusterSupport.extractEntityId,
      extractShardId = GroupClusterSupport.extractShardId)

    // 用户
    val userRegion = ClusterSharding(system).start(
      typeName = UserClusterSupport.shardName,
      entityProps = Group.props(),
      settings = ClusterShardingSettings(system),
      extractEntityId = UserClusterSupport.extractEntityId,
      extractShardId = UserClusterSupport.extractShardId)

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
