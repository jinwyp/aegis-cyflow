package com.yimei.cflow.integration

import akka.actor.{Actor, ActorLogging, ActorRef, Props, Terminated}
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow.CommandQuery
import com.yimei.cflow.integration.DaemonMaster.{GiveMeModule, QueryTest, RegisterModule}

// 模块注册于协商
object DaemonMaster {

  case class RegisterModule(name: String, actor: ActorRef)

  case class GiveMeModule(name: String)

  case class UnderIdentify()

  def props(moduleProps: Map[String, Props]) = Props(new DaemonMaster(moduleProps))

  case class QueryTest(flowName: String, flowId: String)

}


/**
  * Created by hary on 16/12/3.
  */
class DaemonMaster(moduleProps: Map[String, Props]) extends Actor with ActorLogging with Core {

  var modules: Map[String, ActorRef] = moduleProps.map { entry =>
    log.info(s"开始启动模块 ${entry._1}")
    val m =  context.actorOf(entry._2, entry._1)
    context.watch(m)
    (entry._1 -> m)
  }

  log.info(s"DaemonMaster modules: ${modules.values.map(_.path)}")

  override def receive = {
    // 谁找我要模块, 我就给谁模块
    case GiveMeModule(name) =>
      log.info(s"收到GiveMeModule(${name}) from [${sender().path}]")
      val org = sender()
      modules.get(name).foreach(org ! RegisterModule(name, _) )

    // 测试查询消息
    case QueryTest(flowName, flowId) =>
      log.info(s"收到QueryTest(${flowName}, ${flowId})")
      modules.get(flowName).foreach( _ forward CommandQuery(flowId))

    case Terminated(ref) =>
      val (died, rest) = modules.span(entry => entry._2 == ref);
      modules = rest
      died.foreach { entry =>
        log.warning(s"!!!!!!!!!!!!!!!!!!${entry._1} died")
      }
  }
}

