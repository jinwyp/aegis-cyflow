package com.yimei.cflow.persist

import java.util.UUID

import akka.actor.{Actor, ActorLogging, SupervisorStrategy}
import com.yimei.cflow.persist.Persist.{CreateFlowRequest, CreateFlowResponse}

/**
  * Created by hary on 16/12/1.
  */
class PersistSupervisor extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  def receive = {
    case CreateFlowRequest(userId) =>
      log.info(s"用户${userId}创建persist订单...")
      val orderId = UUID.randomUUID().toString;
      val flowActor = context.actorOf(Persist.props(userId, orderId), orderId)
      sender() ! CreateFlowResponse(orderId, flowActor)
  }
}
