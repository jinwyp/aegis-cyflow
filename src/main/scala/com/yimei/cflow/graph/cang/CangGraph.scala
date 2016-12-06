package com.yimei.cflow.graph.cang

import akka.actor.ActorRef
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.{FlowGraph, GraphBuilder}
import com.yimei.cflow.data.DataMaster.fetch
import com.yimei.cflow.user.UserMaster.ufetch

/**
  * Created by hary on 16/12/1.
  */
object CangGraph extends FlowGraph {

  def getFlowInitial: Decision = V0

  def getFlowJson(state: State) =
    GraphBuilder.jsonGraph(state) { implicit builder =>
      import GraphBuilder._
      V1 ~> E1 ~> V2
      V2 ~> E2 ~> V3
      V3 ~> E3 ~> V4
      V4 ~> E4 ~> V5
      V5 ~> E5 ~> V6
      V3 ~> VoidEdge ~> V7
      builder
    }

  override def getFlowName: String = "ying"

  case object E1 extends Edge with CoreConfig {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      coreSystem.log.info(s"E1开始调度${data_A}, ${data_B}, ${data_C}")
      fetch(data_A, state, modules(module_data))
      fetch(data_B, state, modules(module_data))
      fetch(data_C, state, modules(module_data))
    }

    def check(state: State) = !Array(point_A, point_B, point_C).exists(
      !state.points.contains(_)
    )

    override def toString = "A|B|C"
  }

  case object E2 extends Edge with CoreConfig {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      fetch(data_DEF, state, modules(module_data))
    }

    def check(state: State) = ! dataPointMap(data_DEF).exists(!state.points.contains(_))

    override def toString = "D|E|F"
  }

  case object E3 extends Edge with CoreConfig {
    def schedule(state: State, modules: Map[String, ActorRef]) = ???

    def check(state: State) = ! dataPointMap(data_DEF).exists(!state.points.contains(_))

    override def toString = "D|E|F"
  }

  case object E4 extends Edge with CoreConfig {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      fetch(data_GHK, state,modules(module_data))
    }

    def check(state: State) = ! dataPointMap(data_GHK).exists(!state.points.contains(_))

    override def toString = "G|H|K"
  }

  // fetch data from user module of task_A for point_U_A1, point_U_A2
  case object E5 extends Edge with CoreConfig {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      ufetch(task_A, state, modules(module_user))
    }

    def check(state: State) = ! taskPointMap(task_A).exists(!state.points.contains(_))

    override def toString = "UA1|UA2"
  }

  // fetch data from user module of task_B for point_U_B1, point_U_B2
  case object E6 extends Edge with CoreConfig {
    def schedule(state: State, modules: Map[String, ActorRef]) = {
      ufetch(task_B, state, modules(module_user))
    }

    def check(state: State) = ! taskPointMap(task_B).exists(!state.points.contains(_))

    override def toString = "UB1|UB2"
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
      state.points.filter(entry => dataPointMap(data_DEF).contains(entry._1)).foldLeft(0){ (acc, entry) =>
        acc +  entry._2.value
      } match {
        case 150 => V3
        case _  => FlowFail
      }
    }

    override def toString = "V2"
  }

  case object V3 extends Judge {
    override def in = E3

    override def decide(state: State) = {

      state.points.filter(entry => dataPointMap(data_DEF).contains(entry._1)).foldLeft(0){ (acc, entry) =>
        acc +  entry._2.value
      } match {
        case 150 => V4
        case _  => FlowFail
      }

    }

    override def toString = "V3"
  }

  case object V4 extends Judge {
    override def in = E4

    override def decide(state: State) = {
      println(s"V4 state = $state")
      state.points.filter(entry => dataPointMap(data_GHK).contains(entry._1)).foldLeft(0){ (acc, entry) =>
        acc +  entry._2.value
      } match {
        case 150 => V5
        case m  =>
          println(s"V4 result = $m")
          FlowFail
      }

    }

    override def toString = "V4"
  }

  case object V5 extends Judge {
    override def in = E5

    override def decide(state: State) = {

      state.points.filter(entry => List(point_U_A1, point_U_A2).contains(entry._1)).foldLeft(0){ (acc, entry) =>
        acc +  entry._2.value
      } match {
        case 100 => V6
        case _  => FlowFail
      }

    }

    override def toString = "V5"
  }

  case object V6 extends Judge {
    override def in = E6

    override def decide(state: State) = {

      state.points.filter(entry => List(point_U_B1, point_U_B2).contains(entry._1)).foldLeft(0){ (acc, entry) =>
        acc +  entry._2.value
      } match {
        case 100 => FlowSuccess
        case _  => FlowFail
      }
    }

    override def toString = "V6"
  }

  ///////////////////////////////////////////////////////////////////////////////////////
  //      \ ----------------------> VoidEdge
  //      V0
  //       \ ---------------------> A(data_A) B(data_B) C(data_C)   E1
  //       V1
  //         \--------------------> [D, E, F](data_DEF)             E2
  //         v2
  //          \-------------------> [D, E, F]                       E3
  //          V3
  //           \------------------> [G, H, K]                       E4
  //            V4
  //           /  \---------------> [UA1, UA2](task_A)              E5
  //          V7   V5
  //         /      \-------------> [UB3, UB2](task_B)              E6
  //        V8       V6
  ///////////////////////////////////////////////////////////////////////////////////////

  case object V7 extends Judge {
    override def in = VoidEdge

    override def decide(state: State) = V8

    override def toString = "V7"
  }

  case object V8 extends Judge {
    override def in = VoidEdge

    override def decide(state: State) = FlowSuccess

    override def toString = "V8"
  }

}


