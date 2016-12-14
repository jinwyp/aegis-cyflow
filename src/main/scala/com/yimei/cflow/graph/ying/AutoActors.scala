package com.yimei.cflow.graph.ying

import java.util.{Date, UUID}

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{CommandPoint, CommandPoints, DataPoint}
import com.yimei.cflow.graph.ying.YingConfig._

/**
  * Created by hary on 16/12/1.
  */
// 数据采集器
object AutoActors extends CoreConfig {

//  def registerAll() = {
//    AutoRegistry.register(flow_ying, data_A,   (modules: Map[String, ActorRef]) => Props(new A(modules)),Map(data_A -> dataPointMap(data_A)))
//    AutoRegistry.register(flow_ying, data_B,   (modules: Map[String, ActorRef]) => Props(new B(modules)),Map(data_B -> dataPointMap(data_B)))
//    AutoRegistry.register(flow_ying, data_C,   (modules: Map[String, ActorRef]) => Props(new C(modules)),Map(data_C -> dataPointMap(data_C)))
//    AutoRegistry.register(flow_ying, data_DEF, (modules: Map[String, ActorRef]) => Props(new DEF(modules)),Map(data_DEF -> dataPointMap(data_DEF)))
//    AutoRegistry.register(flow_ying, data_GHK, (modules: Map[String, ActorRef]) => Props(new GHK(modules)),Map(data_GHK -> dataPointMap(data_GHK)))
//  }

  import scala.concurrent.duration._

  def uuid() = UUID.randomUUID().toString

//  def actorProps(modules: Map[String, ActorRef]) =
//    Map[String, Props](
//      data_A -> Props(new A(modules)),
//      data_B -> Props(new B(modules)),
//      data_C -> Props(new C(modules)),
//      data_DEF -> Props(new DEF(modules)),
//      data_GHK -> Props(new GHK(modules))
//    )

  val point = DataPoint("50", Some("memo"), Some("system"), uuid, new Date())

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


  // 同时采集GHK
  class GHK(modules: Map[String, ActorRef])  extends Actor {
    def receive = {
      case CommandAutoTask(flowId,flowType, name) =>
        context.system.scheduler.scheduleOnce(7 seconds, modules(module_flow),
          CommandPoints(flowId, Map( point_G -> point, point_H -> point, point_K -> point)))
    }
  }
}
