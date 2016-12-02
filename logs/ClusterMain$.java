package com.yimei.cflow;

/**
  * Created by hary on 16/12/1.
  */
object ClusterMain {
  def main(args: Array[String]): Unit = {
    if (args.isEmpty)
      startup(Seq("2551", "2552", "0"))
    else
      startup(args)
  }
  // 启动多个ActorSystem!!!!!
  def startup(ports: Seq[String]): Unit = {
    ports foreach { port =>
      val config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).withFallback(ConfigFactory.load())
      val system = ActorSystem("ClusterSystem", config)

      // 只有2551才启动store, 其他的system中,
      // 将向path发送Identify(None)消息(等待ActorIdentity消息后,
      // SharedLeveldbJournal将设置好)
      startupSharedJournal(system, startStore = (port == "2551"),
        path = ActorPath.fromString("akka.tcp://ClusterSystem@127.0.0.1:2551/user/store"))

      // authorList分片, 分片是个actorRef!!!!!
      val persistentRegion: ActorRef = ClusterSharding(system).start(
        typeName        = PersistentFlow.shardName,        // 分片名称
        entityProps     = Persist.props(),                 // 分片actor是什么
        settings        = ClusterShardingSettings(system), // 分片设定
        extractEntityId = PersistentFlow.idExtractor,      // idExtractor   --->
        extractShardId  = PersistentFlow.shardResolver     // shardId       --->
      )
    }

    // 为每个ActorSystem启动共享journal
    def startupSharedJournal(system: ActorSystem, startStore: Boolean, path: ActorPath): Unit = {
      // 只在2551 system上启动SharedLeveldbStore actor
      if (startStore)
        system.actorOf(Props[SharedLeveldbStore], "store")

      // 向store的ActorPath发送 Identify(None)消息
      import system.dispatcher
      implicit val timeout = Timeout(15.seconds)
      val f = (system.actorSelection(path) ? Identify(None))
      f.onSuccess {
        // 收到store actor的应答后, 本系统SharedLeveldbJournal.setStore(ref, system)
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
}
