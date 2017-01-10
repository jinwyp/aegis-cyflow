package com.yimei.cflow.graph.cang.services

import akka.actor.ActorLogging
import akka.camel.Consumer
import com.yimei.cflow.graph.cang.services.FlowService._

/**
  * Created by wangqi on 17/1/10.
  */
object ScheduleTask {

    class CangSchedule extends Consumer with ActorLogging {
      def endpointUri = "quartz://schedule-cang?cron=0/30+*+*+*+*+?"

      def receive = {
        case msg =>
          log.info("开始查询交易情况")
          queryPayResult()
      }
    }

}
