package com.yimei.cflow.point

import java.util.Date

import akka.actor.{Actor, ActorRef, Props, SupervisorStrategy, Terminated}
import com.yimei.cflow.core.Flow.{CommandPoint, CommandPoints, DataPoint}

/**
  * Created by hary on 16/12/1.
  */
// 数据采集器
object DataActors {

  import scala.concurrent.duration._

  def props(flowMaster: ActorRef) = Props(new DataActors(flowMaster))

  @volatile
  var actors: Map[String, ActorRef] = null

  class A extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(
          100 micros,
          sender(),
          CommandPoint(flowId, "A", DataPoint(50, "memo", "hary", new Date()))
        )
    }
  }

  class B extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(200 micros, sender(), CommandPoint(flowId, "B", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class C extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(300 micros, sender(), CommandPoint(flowId, "C", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class D extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(100 micros, sender(), CommandPoint(flowId, "D", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class E extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(200 micros, sender(), CommandPoint(flowId, "E", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class F(flowMaster: ActorRef) extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(
          5 seconds,
          flowMaster,
          CommandPoint(flowId, "F", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  // 同时采集DEF
  class DEF extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(7 seconds, sender(),
          CommandPoints(flowId,
            Map(
              "D" -> DataPoint(50, "memo", "hary", new Date()),
              "E" -> DataPoint(50, "memo", "hary", new Date()),
              "F" -> DataPoint(50, "memo", "hary", new Date())
            )
          )
        )
    }
  }
}

class DataActors(flowMaster: ActorRef) extends Actor {

  import DataActors._

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  synchronized {
    DataActors.actors = Map[String, ActorRef](
      "A" -> context.actorOf(Props[A], "A"),
      "B" -> context.actorOf(Props[B], "B"),
      "C" -> context.actorOf(Props[C], "C"),
      "D" -> context.actorOf(Props[D], "D"),
      "E" -> context.actorOf(Props[E], "E"),
      "F" -> context.actorOf(Props(new F(flowMaster)), "F"),
      "DEF" -> context.actorOf(Props[DEF], "DEF")
    )
  }

  def receive = {
    case Terminated(ref) => ???
  }
}


