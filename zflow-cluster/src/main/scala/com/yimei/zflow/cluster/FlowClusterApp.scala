package com.yimei.zflow.cluster

import akka.actor.ActorSystem
import akka.cluster.sharding.{ClusterSharding, ClusterShardingSettings}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import com.yimei.cflow.engine.auto.AutoMaster
import com.yimei.cflow.id.IdGenerator
import com.yimei.zflow.cluster.support.{FlowClusterSupport, GroupClusterSupport, UserClusterSupport}

/**
  * Created by hary on 16/12/16.
  */
object FlowClusterApp extends FlowClusterSupport
  with GroupClusterSupport
  with UserClusterSupport {

  val common = ConfigFactory.load()

  override val groupNumberOfShards: Int = common.getInt("zflow.shard.group")
  override val userNumberOfShards: Int  = common.getInt("zflow.shard.user")
  override val flowNumberOfShards: Int  = common.getInt("zflow.shard.flow")

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) throw new IllegalArgumentException("请提供节点编号")
    val nodeId = args(0).toInt
    val nodeConfig = ConfigFactory.load(s"node-${nodeId}.conf").withFallback(common)
    startup(nodeConfig)
  }

  def startup(config: Config): Unit = {
    implicit val system = ActorSystem("FlowSystem", config)
    implicit val materializer = ActorMaterializer()

    // Id服务
    val idGenerator = system.actorOf(IdGenerator.props("id"))

    // 自动
    val auto = system.actorOf(AutoMaster.props(Array("abc")))

    // 流程管理 - 需要idGenerator
    val flowRegion = ClusterSharding(system).start(
      typeName = flowShardName,
      entityProps = null,                  //  创建流程的Prop
      settings = ClusterShardingSettings(system),
      extractEntityId = flowExtractEntityId,
      extractShardId = flowExtractShardId)

    // 任务组管理
    val groupRegion = ClusterSharding(system).start(
      typeName = groupShardName,
      entityProps = null,     // 创建任务组Prop
      settings = ClusterShardingSettings(system),
      extractEntityId = groupExtractEntityId,
      extractShardId = groupExtractShardId)

    // 用户任务管理
    val userRegion = ClusterSharding(system).start(
      typeName = userShardName,
      entityProps = null,  // 创建用户任务Prop
      settings = ClusterShardingSettings(system),
      extractEntityId = userExtractEntityId,
      extractShardId = userExtractShardId)

    // 路由
    val all: Route = null;

    // 启动rest服务
    // println(s"http is listening on ${config.getInt("rest.port")}")
    Http().bindAndHandle(all, "0.0.0.0", config.getInt("rest.port"))
  }
}
