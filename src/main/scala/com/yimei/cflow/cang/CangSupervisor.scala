package com.yimei.cflow.cang

import java.util.UUID

import akka.actor.{Actor, ActorLogging, SupervisorStrategy}
import com.yimei.cflow.cang.Cang.{CreateFlowRequest, CreateFlowResponse}

/**
  * Created by hary on 16/12/1.
  */
class CangSupervisor extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  def receive = {
    case CreateFlowRequest(userId) =>
      log.info(s"用户${userId}创建仓押订单...")
      val orderId = UUID.randomUUID().toString;
      val flowActor = context.actorOf(Cang.props(userId, orderId),  orderId)
      sender() !  CreateFlowResponse(orderId, flowActor)
  }
}
