package com.yimei.cflow.core

import java.io.File
import java.lang.reflect.Method

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.graph.ying.YingGraphJar
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.io.Source

case class DefaultVertex(description: String, arrows: Seq[Arrow])

case class GraphConfig(
                        graphJar: String,
                        persistent: Boolean,
                        timeout: Int,
                        initial: String,
                        points: Map[String, String],
                        autoTasks: Map[String, Array[String]],
                        userTasks: Map[String, Array[String]],
                        vertices: Map[String, DefaultVertex],
                        edges: Map[String, Edge]
                      )

object GraphConfigProtocol extends DefaultJsonProtocol with FlowProtocol {
  implicit val defaultVertexFormat = jsonFormat2(DefaultVertex)
  implicit val graphConfigProtocolFormat = jsonFormat9(GraphConfig)
}


/**
  * Created by hary on 16/12/17.
  */
object GraphLoader extends App {

  def loadall() =
    new File("flows")
      .listFiles()
      .filter(_.isDirectory())
      .map(_.getName)
      .foreach(flowType => FlowRegistry.register(flowType, loadGraph(flowType)))

  def getClassLoader(flowType: String) = {
    if (flowType != "ying") {
      val jars: Array[String] = (new File("flows/" + flowType))
        .listFiles()
        .filter(_.isFile())
        .map(_.getPath)
      new java.net.URLClassLoader(jars.map(new File(_).toURI.toURL), this.getClass.getClassLoader)
    } else {
      YingGraphJar.getClass.getClassLoader
    }
  }

  def loadGraph(gFlowType: String): FlowGraph = {
    import GraphConfigProtocol._
    import spray.json._

    val classLoader = getClassLoader(gFlowType)

    var graphConfig = Source.fromInputStream(classLoader.getResourceAsStream(if (gFlowType == "ying") "ying.json" else "flow.json"))
      .mkString
      .parseJson
      .convertTo[GraphConfig]

    graphConfig = graphConfig.copy(edges = graphConfig.edges ++ Map(
      "start" -> Edge( name = "start", begin = "God", end = graphConfig.initial),
      "success" -> Edge( name = "success", end = "success"),
      "fail" -> Edge( name = "fail", end = "success")
      )
    )

    println(graphConfig.toJson.prettyPrint)

    // graphJar class and graphJar object
    val mclass = classLoader.loadClass(graphConfig.graphJar + "$")
    val graphJar = mclass.getField("MODULE$").get(null)

    // auto auto actor behavior from graphJar
    val autoMap = getAutoMap(mclass)

    // deciders from graphJar  + default decider
    var allDeciders: Map[String, State => Seq[Arrow]] =
      graphConfig.vertices.map { entry =>
        (entry._1, entry._2.arrows)
      }.filter(_._2.length == 1) // 暂时只处理长度为1的(非并发执行流)
        .map { e =>
        (e._1, { st: State => Seq(e._2(0)) })
      }
    allDeciders = allDeciders ++ getDeciders(mclass, graphJar) // 用jar中的覆盖配置中的

    // graph intial vertex
    val initial = graphConfig.initial

    // 返回流程
    val g = new FlowGraph {

      override val timeout: Long = graphConfig.timeout

      override val points: Map[String, String] = graphConfig.points

      override val vertices: Map[String, String] = {
        graphConfig.vertices.map{ entry =>
          ( entry._1, entry._2.description)
        }
      }

      override def graph(state: State): Graph = Graph(
        graphConfig.edges,
        graphConfig.vertices.map { entry => (entry._1, entry._2.description) },
        Some(state),
        graphConfig.points,
        graphConfig.userTasks,
        graphConfig.autoTasks
      )

      override val blueprint: Graph = Graph(
        graphConfig.edges,
        graphConfig.vertices.map { entry => (entry._1, entry._2.description) },
        None,
        graphConfig.points,
        graphConfig.userTasks,
        graphConfig.autoTasks
      )

      override val inEdges: Map[String, Array[String]] = graphConfig.edges.groupBy { entry =>
        entry._2.end
      }.map { e =>
        (e._1, e._2.keySet.toArray)
      }


      override val flowInitial: String = initial

      override val flowType: String = gFlowType

      override val userTasks: Map[String, Array[String]] = graphConfig.userTasks

      override val autoTasks: Map[String, Array[String]] = graphConfig.autoTasks

      override val edges: Map[String, Edge] = graphConfig.edges

      override val pointEdges = pointEdgesImpl

      override val autoMethods: Map[String, Method] = autoMap

      override val deciders: Map[String, State => Seq[Arrow]] = allDeciders

      override val moduleJar: AnyRef = graphJar
    }

    g
  }

  def getAutoMap(m: Class[_]) = {
    m.getMethods.filter { method =>
      val ptypes = method.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[CommandAutoTask] &&
        method.getReturnType == classOf[Future[Map[String, String]]]
    }.map { am =>
      (am.getName -> am)
    }.toMap
  }

  def getDeciders(m: Class[_], module: AnyRef) = {
    m.getMethods.filter { method =>
      val ptypes = method.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[State] &&
        method.getReturnType == classOf[Seq[Arrow]]
    }.map { am =>
      val behavior: State => Seq[Arrow] = (state: State) =>
        am.invoke(module, state).asInstanceOf[Seq[Arrow]]
      (am.getName -> behavior)
    }.toMap
  }

}
