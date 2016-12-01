package com.yimei.cflow.cang

import akka.actor.ActorRef
import com.yimei.cflow.Flow._

/**
  * Created by hary on 16/12/1.
  */
object CangGraph {

  import com.yimei.cflow.DataActors.actors

  object R extends Edge {
    def schedule(self: ActorRef, state: State) = {
      actors("R").tell("R", self) // 给R发消息
    }

    def check(state: State) = {
      if (state.points.contains("R")) // 存在R数据点
        true
      else
        false
    }
  }

  object E1 extends Edge {
    def schedule(self: ActorRef, state: State) = {
      actors("A").tell("A", self) // 给R发消息
      actors("B").tell("B", self) // 给R发消息
      actors("C").tell("C", self) // 给R发消息
    }

    def check(state: State) = {
      if (state.points.contains("A") &&
        state.points.contains("B") &&
        state.points.contains("C"))
        true
      else
        false
    }

    override def toString = "A|B|C"
  }

  object E2 extends Edge {
    def schedule(self: ActorRef, state: State) = {
      actors("D").tell("D", self) // 给R发消息
      actors("E").tell("E", self) // 给R发消息
      actors("F").tell("F", self) // 给R发消息
    }

    def check(state: State) = {
      if (state.points.contains("D") &&
        state.points.contains("E") &&
        state.points.contains("F"))
        true
      else
        false
    }

    override def toString = "D|E|F"
  }

  object E3 extends Edge {
    def schedule(self: ActorRef, state: State) = ???

    def check(state: State) = false

    override def toString = "D|E|F"
  }

  object E4 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = false
    override def toString = "D|E|F"
  }

  object E5 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = false
    override def toString = "D|E|F"
  }

  /////////////////
  object V1 extends Judge {
    override def in = R

    override def decide(state: State): Decision = {
      if (state.points("R").value == 50) {
        V2
      } else
        FlowFail
    }

    override def toString = "V1"
  }

  object V2 extends Judge {
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

  object V3 extends Judge {
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

  object V4 extends Judge {
    override def in = E3

    override def decide(state: State) = ???

    override def toString = "V4"
  }

  object V5 extends Judge {
    override def in = E4

    override def decide(state: State) = ???

    override def toString = "V5"
  }

  object V6 extends Judge {
    override def in = E5

    override def decide(state: State) = ???

    override def toString = "V6"
  }


  import com.yimei.cflow.FlowGraph._
  import spray.json._
  def cangJsonGraph(state: State) = {
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


