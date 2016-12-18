package com.yimei.cflow.graph.ying

import akka.actor.Actor.Receive
import akka.actor.ActorRef
import com.yimei.cflow.core.Flow.{Arrow, Graph, State}
import com.yimei.cflow.core.{AutoActor, FlowProtocol, FlowRegistry, GraphJar}
import com.yimei.cflow.user.User

/**
  * Created by hary on 16/12/17.
  */
object YingGraphJar extends GraphJar with FlowProtocol {

  import com.yimei.cflow.core.PointUtil._

  override val flowType: String = "ying"

  override def getDeciders: Map[String, (State) => Arrow] = Map(
    "V0" -> v0,
    "V1" -> v1,
    "V2" -> v2,
    "V3" -> v3,
    "V4" -> v4,
    "V5" -> v5
  )

  def v0(state: State): Arrow = {
    val unwrapped = state.points("Hello").unwrap[Graph]
    Arrow("v1", None)
  }

  def v1(state: State): Arrow = ???
  def v2(state: State): Arrow = ???
  def v3(state: State): Arrow = ???
  def v4(state: State): Arrow = ???
  def v5(state: State): Arrow = ???


  class AutoA(modules: Map[String, ActorRef]) extends AutoActor(modules) {
    override def receive: Receive = ???
  }

  class AutoB(modules: Map[String, ActorRef]) extends AutoActor(modules) {
    override def receive: Receive = ???
  }

  class AutoC(modules: Map[String, ActorRef]) extends AutoActor(modules) {
    override def receive: Receive = ???
  }

}
