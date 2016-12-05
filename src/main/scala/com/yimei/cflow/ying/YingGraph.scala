package com.yimei.cflow.ying

import akka.actor.{ActorLogging, ActorRef}
import com.yimei.cflow.config.Core
import com.yimei.cflow.core.Flow._

/**
  * Created by hary on 16/12/1.
  */
object YingGraph {

  import com.yimei.cflow._

  import com.yimei.cflow.data.DataMaster.{fetch}
  import com.yimei.cflow.user.UserMaster._

  case object E1 extends Edge with Core {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = {

      system.log.info(s"E1开始调度${data_A}, ${data_B}, ${data_C}")
      fetch(data_A, state, module_ying, modules(module_data))
      fetch(data_B, state, module_ying, modules(module_data))
      fetch(data_C, state, module_ying, modules(module_data))
    }

    def check(state: State) = {
      if (!state.points.contains(point_A)) {
        println("need A!!!!");
        false
      }
      else if (!state.points.contains(point_B)) {
        println("need B!!!!");
        false
      }
      else if (!state.points.contains(point_C)) {
        println("need C!!!!");
        false
      }
      else true
    }

    override def toString = "A|B|C"
  }

  case object E2 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = {
      fetch(data_DEF, state, module_ying, modules(module_data))
    }

    def check(state: State) = {
      if (!state.points.contains(point_D)) {
        println("need D!!!!");
        false
      }
      else if (!state.points.contains(point_E)) {
        println("need E!!!!");
        false
      }
      else if (!state.points.contains(point_F)) {
        println("need F!!!!");
        false
      }
      else true
    }

    override def toString = "D|E|F"
  }

  case object E3 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  case object E4 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  case object E5 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  case object E6 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  /////////////////
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
      if (state.points(point_A).value == 50 &&
        state.points(point_B).value == 50 &&
        state.points(point_C).value == 50) {
        V3
      } else
        FlowFail
    }

    override def toString = "V2"
  }

  case object V3 extends Judge {
    override def in = E3

    override def decide(state: State) = {
      if (state.points(point_D).value == 50 &&
        state.points(point_E).value == 50 &&
        state.points(point_F).value == 50) {
        V4
      } else
        FlowFail
    }

    override def toString = "V3"
  }

  case object V4 extends Judge {
    override def in = E4

    override def decide(state: State) = V5

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


