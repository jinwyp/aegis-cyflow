package com.yimei.cflow.graph.cang

import akka.actor.ActorRef
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.{FlowGraph, GraphBuilder}
import com.yimei.cflow.data.DataMaster._
import com.yimei.cflow.config.GlobalConfig._


/**
  * Created by hary on 16/12/1.
  */
object CangGraph extends FlowGraph with CoreConfig {


  override def getFlowInitial: Decision = V0

  override def getFlowJson(state: State): String =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      import GraphBuilder._
      V1 ~> E3 ~> V4
      V1 ~> E1 ~> V2
      V4 ~> E4 ~> V5
      V4 ~> E5 ~> V6
      V2 ~> E2 ~> V3
      builder
    }

  override def getFlowName: String = "cang"

  case object E1 extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      fetch(data_A, state, modules(module_data))
      fetch(data_B, state, modules(module_data))
      fetch(data_C, state, modules(module_data))
    }

    def check(state: State) = {
      if (!state.points.contains(point_A)) {
        coreSystem.log.debug("E1 need A!!!!");
        false
      }
      else if (!state.points.contains(point_B)) {
        coreSystem.log.debug("need B!!!!");
        false
      }
      else if (!state.points.contains(point_C)) {
        coreSystem.log.debug("need C!!!!");
        false
      }
      else true
    }

    override def toString = "A|B|C"
  }

  case object E2 extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      fetch(data_DEF, state, modules(module_data))
    }

    def check(state: State) = {
      if (!state.points.contains(point_D)) {
        coreSystem.log.debug("need D!!!!");
        false
      }
      else if (!state.points.contains(point_E)) {
        coreSystem.log.debug("need E!!!!");
        false
      }
      else if (!state.points.contains(point_F)) {
        coreSystem.log.debug("need F!!!!");
        false
      }
      else true
    }

    override def toString = "D|E|F"
  }

  case object E3 extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  case object E4 extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  case object E5 extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  case object E6 extends Edge {
    def schedule(state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = true

    override def toString = "D|E|F"
  }

  /////////////////
  case object V0 extends Judge {
    override def in = VoidEdge

    override def decide(state: State): Decision = {
      coreSystem.log.info("V0 judge to V1")
      V1
    }

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
        coreSystem.log.info("V2 judge to V3")
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
        coreSystem.log.info("V3 judge to V4")
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
