package com.yimei.cflow;

/**
  * Created by hary on 16/12/1.
  */
object CangTest extends App with Core {


  // 运行cang demo
  cangDemo()

  def cangDemo(flowId: String = UUID.randomUUID().toString): Unit = {

    println("intialize system...")

    val system = ActorSystem("RiskSystem")

    // 启动仓押流程管理器
    lazy val cang: ActorRef = CangSupervisor.start(data)

    // 启动采集器...
    lazy val data: ActorRef = DataActors.start(cang)

    import com.yimei.cflow.core.Flow.CommandQuery

    val queryActor = system.actorOf(Props(new QueryActor(cang)), "queryActor")
    system.scheduler.schedule(2 seconds, 13 seconds, queryActor, CommandQuery(flowId))

  }

  class QueryActor(ying: ActorRef) extends Actor {
    def receive = {
      case CommandQuery(flowId) =>
        ying ! CommandQuery(flowId)
      case jsonGraph: String =>
      // system.log.info(s"$state")
      // context.system.log.info(s"收到消息json = $jsonGraph")
    }
  }

}
