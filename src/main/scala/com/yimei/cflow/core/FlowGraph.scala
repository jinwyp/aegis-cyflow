package com.yimei.cflow.core

/**
  * Created by hary on 16/12/1.
  */
object FlowGraph {
  import com.yimei.cflow.core.Flow._

  case class EdgeLine(begin: Decision, description: Edge, end: Decision)

  trait Tree
  case class Node(value: Decision, children: List[Tree]) extends Tree
  case class Leaf(value: Decision) extends Tree
  case class Graph(tree: Tree, edges: List[EdgeLine], state: State)

  import FlowProtocol._
  import spray.json._

  implicit object TreeFormat extends JsonFormat[Tree] {
    def write(obj: Tree) = {
      obj match {
        case n: Node => nodeFormat.write(n)
        case l: Leaf => leafFormat.write(l)
      }
    }

    def read(jsValue: JsValue) = ???
  }

  implicit val nodeFormat = jsonFormat2(Node)
  implicit val leafFormat = jsonFormat1(Leaf)
  implicit val edgeLineFormat = jsonFormat3(EdgeLine)
  implicit val graphFormat = jsonFormat3(Graph)

}
