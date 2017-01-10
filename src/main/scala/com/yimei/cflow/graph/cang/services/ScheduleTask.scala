package com.yimei.cflow.graph.cang.services

import akka.actor.ActorLogging
import akka.camel.Consumer
import com.yimei.cflow.graph.cang.services.FlowService._

/**
  * Created by wangqi on 17/1/10.
  */
object ScheduleTask {

    class CiticSchedule extends Consumer with ActorLogging {
      def endpointUri = "quartz://schedule-citic?cron=1+0+*+*+*+?"

      def receive = {
        case msg =>
          log.info("开始查询交易情况")
          queryPayResult()
      }
    }

}
