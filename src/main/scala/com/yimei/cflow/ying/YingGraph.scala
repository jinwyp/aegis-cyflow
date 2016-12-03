package com.yimei.cflow.ying

import akka.actor.ActorRef
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.point.DataActors

/**
  * Created by hary on 16/12/1.
  */
object YingGraph {

  import DataActors.actors

  case object E1 extends Edge {
    def schedule(self: ActorRef, state: State) = {
      actors("A").tell(state.flowId, self) // 给R发消息
      actors("B").tell(state.flowId, self) // 给R发消息
      actors("C").tell(state.flowId, self) // 给R发消息
    }

    def check(state: State) = {
      if (!state.points.contains("A")) {
        println("need A!!!!");
        false
      }
      else if (!state.points.contains("B")) {
        println("need B!!!!");
        false
      }
      else if (!state.points.contains("C")) {
        println("need C!!!!");
        false
      }
      else true
    }

    override def toString = "A|B|C"
  }

  case object E2 extends Edge {
    def schedule(self: ActorRef, state: State) = {
      actors("D").tell(state.flowId, self) // 给R发消息
      actors("E").tell(state.flowId, self) // 给R发消息
      actors("F").tell(state.flowId, self) // 给R发消息
      // actors("DEF").tell(state.flowId, self)
    }

    def check(state: State) = {
      if (!state.points.contains("D")) { println("need D!!!!"); false }
      else if (!state.points.contains("E")) { println("need E!!!!"); false }
      else if (!state.points.contains("F")) { println("need F!!!!"); false }
      else true
    }

    override def toString = "D|E|F"
  }

  case object E3 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = true
    override def toString = "D|E|F"
  }

  case object E4 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = true
    override def toString = "D|E|F"
  }

  case object E5 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = true
    override def toString = "D|E|F"
  }

  case object E6 extends Edge {
    def schedule(self: ActorRef, state: State) = ???
    def check(state: State) = true
    override def toString = "D|E|F"
  }

  case object V0 extends Judge {
    override def in = VoidEdge

    override def decide(state: State): Decision = V1

    override def toString = "V0"
  }

  case object V1 extends Judge {
    override def in = E1

    override def decide(state: State): Decision = V2

    override def toString = "V1"
  }

  case object V2 extends Judge {
    override def in = E2

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
    override def in = E3

    override def decide(state: State) = {
      if (state.points("D").value == 50 &&
        state.points("E").value == 50 &&
        state.points("F").value == 50) {
        V4
      } else
        FlowFail
    }

    override def toString = "V3"
  }

  case object V4 extends Judge {
    override def in = E4

    override def decide(state: State) = V7

    override def toString = "V4"
  }

  case object V5 extends Judge {
    override def in = E5

    override def decide(state: State) = V6

    override def toString = "V5"
  }

  case object V6 extends Judge {
    override def in = E6
    override def decide(state: State) = FlowSuccess
    override def toString = "V6"
  }

  //////////////////////////////////////////////
  //       V1
  //         \
  //         v2
  //          \
  //          V3
  //           \
  //            V4
  //           /  \
  //          V7   V5
  //         /      \
  //        V8       V6
  //////////////////////////////////////////////

  case object V7 extends Judge {
    override def in = VoidEdge
    override def decide(state: State) = V8
    override def toString = "V7"
  }

  case object V8 extends Judge {
    override def in = VoidEdge
    override def decide(state: State) = FlowSuccess
    override def toString = "V6"
  }
}


