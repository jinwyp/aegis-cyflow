package com.yimei.cflow.cang

import akka.actor.{ActorLogging, ActorRef}
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.data.DataMaster.GetPoint

/**
  * Created by hary on 16/12/1.
  */
object CangGraph {

  import com.yimei.cflow._


  def getData(name: String, self: ActorRef, state:State, modules: Map[String, ActorRef]) = {
    modules(module_data).tell(GetPoint(modules(module_cang), state.flowId, name), modules(module_cang))
  }


  case object E1 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = {
      getData(data_A, self, state, modules)
      getData(data_B, self, state, modules)
      getData(data_C, self, state, modules)
    }

    def check(state: State) = {
      if (!state.points.contains(data_A)) {
        println("need A!!!!");
        false
      }
      else if (!state.points.contains(data_B)) {
        println("need B!!!!");
        false
      }
      else if (!state.points.contains(data_C)) {
        println("need C!!!!");
        false
      }
      else true
    }

    override def toString = "A|B|C"
  }

  case object E2 extends Edge {
    def schedule(self: ActorRef, state: State, modules: Map[String, ActorRef]) = {

//      modules(module_data).tell(GetPoint(modules(module_cang), state.flowId, data_D), modules(module_cang))
//      modules(module_data).tell(GetPoint(modules(module_cang), state.flowId, data_E), modules(module_cang))
//      modules(module_data).tell(GetPoint(modules(module_cang), state.flowId, data_F), modules(module_cang))
      getData(data_DEF, self, state, modules)
    }

    def check(state: State) = {
      if (!state.points.contains(data_D)) {
        println("need D!!!!"); false
      }
      else if (!state.points.contains(data_E)) {
        println("need E!!!!"); false
      }
      else if (!state.points.contains(data_F)) {
        println("need F!!!!"); false
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
      if (state.points(data_A).value == 50 &&
        state.points(data_B).value == 50 &&
        state.points(data_C).value == 50) {
        V3
      } else
        FlowFail
    }

    override def toString = "V2"
  }

  case object V3 extends Judge {
    override def in = E3

    override def decide(state: State) = {
      if (state.points(data_D).value == 50 &&
        state.points(data_E).value == 50 &&
        state.points(data_F).value == 50) {
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

}
