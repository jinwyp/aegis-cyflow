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

  @volatile
  var actors: Map[String, ActorRef] = null

  class A extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(2 seconds, sender(), CommandPoint(flowId, "A", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class B extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(3 seconds, sender(), CommandPoint(flowId, "B", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class C extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(4 seconds, sender(), CommandPoint(flowId, "C", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class D extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(5 seconds, sender(), CommandPoint(flowId, "D", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class E extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(6 seconds, sender(), CommandPoint(flowId, "E", DataPoint(50, "memo", "hary", new Date())))
    }
  }

  class F extends Actor {
    implicit val dispatcher = context.dispatcher
    def receive = {
      case flowId: String =>
        context.system.scheduler.scheduleOnce(7 seconds, sender(), CommandPoint(flowId, "F", DataPoint(50, "memo", "hary", new Date())))
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

class DataActors extends Actor {

  import DataActors._

  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  synchronized {
    DataActors.actors = Map[String, ActorRef](
      "A" -> context.actorOf(Props[A], "A"),
      "B" -> context.actorOf(Props[B], "B"),
      "C" -> context.actorOf(Props[C], "C"),
      "D" -> context.actorOf(Props[D], "D"),
      "E" -> context.actorOf(Props[E], "E"),
      "F" -> context.actorOf(Props[F], "F"),
      "DEF" -> context.actorOf(Props[DEF], "DEF")
    )
  }

  def receive = {
    case Terminated(ref) => ???
  }
}


