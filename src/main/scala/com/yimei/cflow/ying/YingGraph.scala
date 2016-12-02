package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef}
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.{Flow, FlowGraph}
import com.yimei.cflow.point.DataActors

/**
  * Created by hary on 16/12/1.
  */
object YingGraph {

  import DataActors.actors

//  case object R extends Edge {
//    def schedule(self: ActorRef, state: State) = {
//      actors("R").tell(state.flowId, self) // 给R发消息
//    }
//    def check(state: State) = {
//      if (state.points.contains("R")) // 存在R数据点
//        true
//      else
//        false
//    }
//    override def toString = "R"
//  }

  case object E1 extends Edge {
    def schedule(self: ActorRef, state: State) = {
      actors("A").tell(state.flowId,  self) // 给R发消息
      actors("B").tell(state.flowId,  self) // 给R发消息
      actors("C").tell(state.flowId,  self) // 给R发消息
    }

    def check(state: State) = {
      if (! state.points.contains("A") ) { println("need A!!!!"); false }
      else if (! state.points.contains("B") ) { println("need B!!!!"); false }
      else if (! state.points.contains("C"))  { println("need C!!!!"); false }
      else true
    }

    override def toString = "A|B|C"
  }

  case object E2 extends Edge {
    def schedule(self: ActorRef, state: State) = {

//      actors("D").tell(state.flowId,  self)
//      actors("E").tell(state.flowId,  self)
//      actors("F").tell(state.flowId,  self)

      actors("DEF").tell(state.flowId,  self)

      // 用户采集!!!!
    }

    def check(state: State) = {
      if (! state.points.contains("D") ) { println("need D!!!!"); false }
      else if (! state.points.contains("E") ) { println("need E!!!!"); false }
      else if (! state.points.contains("F"))  { println("need F!!!!"); false }
      else true
    }

    override def toString = "D|E|F"
  }

  case object E3 extends Edge {
    def schedule(self: ActorRef, state: State) = ???

    def check(state: State) = false

    override def toString = "D|E|F"
  }

  case object E4 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = false
    override def toString = "D|E|F"
  }

  case object E5 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = false
    override def toString = "D|E|F"
  }

  /////////////////
  case object V1 extends Judge {
    override def in = InitialEdge
    override def decide(state: State): Decision = {
        V2
    }
    override def toString = "V1"
  }

  case object V2 extends Judge {

    override def in = E1

    override def decide(state: State): Decision = {
      if (state.points("A").value == 50 &&
        state.points("B").value == 50 &&
        state.points("C").value == 50) {
        V3
      } else
        FlowFail
    }

    override def toString = "V2"
  }

  case object V3 extends Judge {
    override def in = E2

    override def decide(state: State) = {
      if (state.points("D").value == 50 &&
        state.points("E").value == 50 &&
        state.points("F").value == 50) {
        FlowSuccess
      } else
        FlowFail
    }

    override def toString = "V3"
  }

  case object V4 extends Judge {
    override def in = E3

    override def decide(state: State) = ???

    override def toString = "V4"
  }

  case object V5 extends Judge {
    override def in = E4

    override def decide(state: State) = ???

    override def toString = "V5"
  }

  case object V6 extends Judge {
    override def in = E5

    override def decide(state: State) = ???

    override def toString = "V6"
  }


  ///////////////////////////////////////////////////////////////////
  // FlowGraph
  ///////////////////////////////////////////////////////////////////
  import FlowGraph._
  import spray.json._
  def yingJsonGraph(state: State) = {
    val t5 = Leaf(V5)
    val t6 = Leaf(V6)
    val t3 = Leaf(V3)
    val t4 = Node(V4, List(t5, t6))
    val t2 = Node(V2, List(t3))
    val t1 = Node(V1, List(t2, t4))

    val edges = List(
      EdgeLine(V1, E3, V4),
      EdgeLine(V1, E1, V2),
      EdgeLine(V4, E4, V5),
      EdgeLine(V4, E5, V6),
      EdgeLine(V2, E2, V3)
    )

    Graph(t1, edges, state)
  }.toJson.toString
}


