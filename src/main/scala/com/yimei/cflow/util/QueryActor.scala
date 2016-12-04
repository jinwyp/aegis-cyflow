package com.yimei.cflow.util

import akka.actor.{Actor, ActorRef}
import com.yimei.cflow.integration.DaemonMaster.QueryTest

/**
  * Created by hary on 16/12/4.
  */
class QueryActor(daemon: ActorRef) extends Actor {

  var first = 0

  def receive = {
    case test @ QueryTest(flowName, flowId, userId) =>
      if ( first == 0  ) {
        daemon ! test
        first = 1
      } else {
        daemon ! QueryTest(flowName, flowId)
      }
    case jsonGraph: String =>
      context.system.log.info(s"收到消息json = $jsonGraph")
  }
}
