//package com.yimei.cflow.graph.ying2
//
//import akka.actor.ActorRef
//import com.yimei.cflow.core.Flow.{Arrow, Graph, State}
//import com.yimei.cflow.core.FlowRegistry.AutoProperty
//import com.yimei.cflow.core.{AutoActor, FlowProtocol, GraphJar}
//
///**
//  * Created by hary on 16/12/17.
//  */
//object YingGraphJar extends GraphJar with FlowProtocol {
//
//  import com.yimei.cflow.core.PointUtil._
//
//  val flowType: String = "ying"
//
//  override def getDeciders: Map[String, (State) => Arrow] = Map(
//    "V0" -> v0,
//    "V1" -> v1,
//    "V2" -> v2,
//    "V3" -> v3,
//    "V4" -> v4,
//    "V5" -> v5
//  )
//  override def getAutoProperties = ???
//
//
//  /////////////////////////////////////////////////////////////////////////
//  // 私有实现!!!!!!!
//  /////////////////////////////////////////////////////////////////////////
//
//  def v0(state: State): Arrow = {
//    val unwrapped = state.points("Hello").unwrap[Graph]
//    Arrow("v1", None)
//  }
//
//  def v1(state: State): Arrow = {
//    Arrow("v1", ???)   // todo:  需要调整decider接口, 不然无法写
//  }
//
//  def v2(state: State): Arrow = ???
//
//  def v3(state: State): Arrow = ???
//
//  def v4(state: State): Arrow = ???
//
//  def v5(state: State): Arrow = ???
//
//
//  //////////////////////////////////////////////////////////////////////////////////////////
//  // 需要依据配置文件中配置得AutoTask名称命名这些Actor的名称!!!!!!!!
//  //////////////////////////////////////////////////////////////////////////////////////////
//  class AutoA(modules: Map[String, ActorRef]) extends AutoActor(modules) {
//    override def receive: Receive = ???
//  }
//
//  class AutoB(modules: Map[String, ActorRef]) extends AutoActor(modules) {
//    override def receive: Receive = ???
//  }
//
//  class AutoC(modules: Map[String, ActorRef]) extends AutoActor(modules) {
//    override def receive: Receive = ???
//  }
//
//}
