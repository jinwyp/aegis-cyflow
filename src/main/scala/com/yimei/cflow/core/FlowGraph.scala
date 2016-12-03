package com.yimei.cflow.core

import com.yimei.cflow.core.Flow.Decision
import spray.json.{JsValue, JsonFormat}

/**
  * Created by hary on 16/12/1.
  */
object FlowGraph {

  import com.yimei.cflow.core.Flow._

  case class EdgeLine(begin: Decision, description: Edge, end: Decision)

  case class Graph(edges: List[EdgeLine], state: State)

  import FlowProtocol._

  implicit val edgeLineFormat = jsonFormat3(EdgeLine)
  implicit val graphFormat = jsonFormat2(Graph)
}

/**
  *
  * import GraphBuilder._
  * GraphBuilder.create(state){ implicit builder =>
  * V1 ~> E3 ~> V4
  * V1 ~> E1 ~> V2
  * V4 ~> E4 ~> V5
  * V4 ~> E5 ~> V6
  * V2 ~> E2 ~> V3
  * builder
  * }
  */
object GraphBuilder {

  import FlowGraph._
  import com.yimei.cflow.core.Flow._

  implicit class Ops(v: Decision) {
    def ~>(e: Edge)(implicit builder: GraphBuilder) = new OpsVE(v, e)
  }

  class OpsVE(vv: Decision, e: Edge) {
    def ~>(v: Decision)(implicit builder: GraphBuilder) = builder.lines = EdgeLine(vv, e, v) :: builder.lines
  }

  def jsonGraph(state: State)(routine: GraphBuilder => GraphBuilder): String = {
    import spray.json._

    val builder = new GraphBuilder(List.empty[EdgeLine]);
    routine(builder)
    Graph(builder.lines, state).toJson.toString
  }

  class GraphBuilder(var lines: List[EdgeLine])

}

object TreeSprayJson {

  import FlowProtocol._

  trait Tree

  case class Node(value: Decision, children: List[Tree]) extends Tree

  case class Leaf(value: Decision) extends Tree

  implicit object TreeFormat extends JsonFormat[Tree] {
    def write(obj: Tree) = {
      obj match {
        case n: Node => nodeFormat.write(n)
        case l: Leaf => leafFormat.write(l)
      }
    }

    def read(jsValue: JsValue) = ???  // 不实现
  }

  implicit val nodeFormat = jsonFormat2(Node)
  implicit val leafFormat = jsonFormat1(Leaf)
}


