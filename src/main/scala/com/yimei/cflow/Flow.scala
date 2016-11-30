import java.util.Date

import Flow.{Command, DataPoint, Judge}
import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}

import scala.concurrent.Future

object Flow {

  case class Command(name: String, point: DataPoint)

  // 事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event

  case class DecisionUpdated(decision: Decision) extends Event

  // 状态
  case class State(points: Map[String, DataPoint], decision: Decision)

  case class DataPoint(value: Int, memo: String, operator: String, timestamp: Date)

  //
  trait Edge {
    def schedule(state: State): Unit

    // 发起哪些数据采集
    def check(state: State): Boolean // 如何判断edge完成
  }

  trait Decision {
    def run(state: State): Decision
  }

  trait Decided extends Decision

  case object Success extends Decided {
    def run(state: State) = Success
  }

  case object Fail extends Decided {
    def run(state: State) = Fail
  }

  case object Todo extends Decided {
    def run(state: State) = Todo
  }

  abstract class Judge extends Decision {
    // 如edge
    def in: Edge

    // 计算结果
    def run(state: State): Decision = {
      if (!in.check(state)) {
        Todo
      } else {
        decide(state)
//        match {
//          case Left(d) => d
//          case Right(j) => j
//        }
      }
    }

    // 觉得是走那个分支, 还是返回成功, 失败
    // def decide(state: State): Either[Decided, Judge]
    def decide(state: State):Decision
  }
}

// 抽象流程
abstract class Flow extends Actor {

  import Flow._

  // 数据点名称 -> 数据点值
  var state: State

  //
  def update(ev: Event) = {
    ev match {
      case PointUpdated(name, point) => state = state.copy(points = state.points + (name -> point))
      case DecisionUpdated(d) => state = state.copy(decision = d)
    }
  }

  // 持久化actor 接收命令
  //  def recieveCommand = {
  //	case Command(name, data) =>
  //        persist(PointUpdated(name, data)){ ev =>
  //		  update(ev)
  //	      val decidor = state.decidor.run(state)
  //	      decidor match {
  //		    case j: Judge   =>  persist(DecisionUpdated(j)) { update(_) }
  //	      }
  //	   }
  //  }

  // 一般actor
  def receive = {
    case Command(name, data) =>
      update(PointUpdated(name, data))
      val decidor = state.decision.run(state)
      decidor match {
        case j: Judge =>
          update(DecisionUpdated(j))
          j.in.schedule(state)
        case a =>
          println(a)
          println(state.points)
      }
  }

//  // 恢复
//  def recieveRecover = {
//    case ev: Event => update(ev)
//  }

}

class CYFlow extends Flow with ActorLogging { outer =>

  import DataActors._
  import Flow.{DataPoint, Edge, State}

  var state = State(Map[String, DataPoint](), V1)

  val actors = Map[String, ActorRef](
    "R" -> context.actorOf(Props[A], "R"),
    "A" -> context.actorOf(Props[A], "A"),
    "B" -> context.actorOf(Props[B], "B"),
    "C" -> context.actorOf(Props[C], "C"),
    "D" -> context.actorOf(Props[D], "D"),
    "E" -> context.actorOf(Props[E], "E"),
    "F" -> context.actorOf(Props[F], "F")
  )

  object R extends Edge {
    def schedule(state: State) = {
      actors("R").tell("R", outer.self) // 给R发消息
    }

    def check(state: State) = {
      if(outer.state.points.contains("R")) // 存在R数据点
        true
      else
        false
    }
  }

  object E1 extends Edge {
    def schedule(state: State) = {
      actors("A").tell("A", outer.self) // 给R发消息
      actors("B").tell("B", outer.self) // 给R发消息
      actors("C").tell("C", outer.self) // 给R发消息
    }

    def check(state: State) = {
      if(outer.state.points.contains("A") &&
        outer.state.points.contains("B") &&
        outer.state.points.contains("C"))
        true
      else
        false
    }
  }

  object E2 extends Edge {
    def schedule(state: State) = {
      actors("D").tell("D", outer.self) // 给R发消息
      actors("E").tell("E", outer.self) // 给R发消息
      actors("F").tell("F", outer.self) // 给R发消息
    }

    def check(state: State) = {
      if(outer.state.points.contains("D") &&
        outer.state.points.contains("E") &&
        outer.state.points.contains("F"))
        true
      else
        false
    }
  }

  object E3

  object E4

  /////////////////
  object V1 extends Judge {
    import Flow._
    override def in = R
    override def decide(state: State): Decision= {
      println("V1决策点信息收集R完成,开始决策")
      if(state.points("R").value==50){
        println("V1决策完成,结果为:"+V2)
        V2
      } else
        Fail
    }
  }

  object V2 extends Judge {
    import Flow._
    override def in = E1
    override def decide(state: State): Decision = {
      println("V2决策点信息收集A,B,C完成,开始决策")
      if(state.points("A").value==50 &&
         state.points("B").value==50 &&
         state.points("C").value==50 ) {
        println("V2决策完成,结果为:"+V3)
        V3
      } else
        Fail
    }
  }

  object V3 extends Judge {
    import Flow._
    override def in = E2
    override def decide(state: State) = {
      println("V3决策点信息收集D,E,F完成,开始决策")
      if(state.points("D").value==50 &&
        state.points("E").value==50 &&
        state.points("F").value==50 ) {
        println("V3决策完成,结果为:"+Success)
        Success
      } else
        Fail
    }
  }

  object V4 extends Judge {
    override def in = ???
    override def decide(state: State) = ???
  }

  object V5 extends Judge {
    override def in = ???
    override def decide(state: State) = ???
  }

}

// 数据采集器
object DataActors {

  import Flow._

  class R extends Actor {
    override def receive = {
      case _ => sender() ! Command("R", DataPoint(50, "memo", "hary", null))
    }
  }

  class A extends Actor {
    def receive = {
      case _ => sender() ! Command("A", DataPoint(50, "memo", "hary", null))
    }
  }

  class B extends Actor {
    def receive = {
      case _ => sender() ! Command("B", DataPoint(50, "memo", "hary", null))
    }
  }

  class C extends Actor {
    def receive = {
      case _ => sender() ! Command("C", DataPoint(50, "memo", "hary", null))
    }
  }

  class D extends Actor {
    def receive = {
      case _ => sender() ! Command("D", DataPoint(50, "memo", "hary", null))
    }
  }

  class E extends Actor {
    def receive = {
      case _ => sender() ! Command("E", DataPoint(50, "memo", "hary", null))
    }
  }

  class F extends Actor {
    def receive = {
      case _ => sender() ! Command("F", DataPoint(50, "memo", "hary", null))
    }
  }

}

object Main extends App {
  println("hello world")
  val system = ActorSystem("Hello")
  val a: ActorRef = system.actorOf(Props[CYFlow], "CYFlow")
  a ! Command("R", DataPoint(50, "memo", "hary", null))
}
