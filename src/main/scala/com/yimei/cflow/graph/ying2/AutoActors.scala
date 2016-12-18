package com.yimei.cflow.graph.ying2

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandPoint, CommandPoints, DataPoint}
import com.yimei.cflow.graph.ying2.YingConfig._

/**
  * Created by hary on 16/12/1.
  */
// 数据采集器
object AutoActors extends CoreConfig {

  import scala.concurrent.duration._

  def uuid() = UUID.randomUUID().toString


  val point = DataPoint("50", Some("memo"), Some("system"), uuid, new Date().getTime)

  class A(modules: Map[String, ActorRef]) extends Actor with ActorLogging {
    def receive = {
      case CommandAutoTask(flowId,flowType, name) =>
        context.system.scheduler.scheduleOnce( 100 millis, modules(module_flow), CommandPoint(flowId, "A", point))
    }
  }

  class B(modules: Map[String, ActorRef])  extends Actor {
    // println(s"modules in B is $modules")
    def receive = {
      case CommandAutoTask(flowId, flowType,name) =>
        context.system.scheduler.scheduleOnce(100 millis, modules(module_flow), CommandPoint(flowId, "B", point))
    }
  }

  class C(modules: Map[String, ActorRef])  extends Actor {
    def receive = {
      case CommandAutoTask(flowId,flowType, name) =>
        context.system.scheduler.scheduleOnce(100 millis, modules(module_flow), CommandPoint(flowId, "C", point))
    }
  }

  class DEF(modules: Map[String, ActorRef])  extends Actor {
    def receive = {
      case CommandAutoTask(flowId,flowType, name) =>
        context.system.scheduler.scheduleOnce(100 millis, modules(module_flow),
          CommandPoints(flowId, Map( point_D -> point, point_E -> point, point_F -> point)))
    }
  }
}

