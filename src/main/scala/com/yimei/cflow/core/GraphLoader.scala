package com.yimei.cflow.core

import java.io.File
import java.lang.reflect.Method

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.graph.ying.YingGraphJar
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future
import scala.io.Source

case class GraphConfig(
                        graphJar: String,
                        persistent: Boolean,
                        timeout: Int,
                        initial: String,
                        poinsts: Map[String, String],
                        autoTasks: Map[String, Array[String]],
                        userTasks: Map[String, Array[String]],
                        deciders: Map[String, String],
                        edges: Map[String, EdgeDescription]
                      )

object GraphConfigProtocol extends DefaultJsonProtocol with FlowProtocol {
  implicit val graphConfigProtocolFormat = jsonFormat9(GraphConfig)
}


/**
  * Created by hary on 16/12/17.
  */
object GraphLoader extends App {

  def loadall() =
    new File("flows").listFiles()
      .filter(_.isDirectory())
      .map(_.getName)
      .foreach(flowType => FlowRegistry.register(flowType, loadGraph(flowType)))

  def getClassLoader(flowType: String) = {

    if ( flowType == "ying") {
      // get jars
      val jars: Array[String] = (new File("flows/" + flowType))
        .listFiles()
        .filter(_.isFile())
        .map(_.getPath)

      // create classloader
      new java.net.URLClassLoader(jars.map(new File(_).toURI.toURL), this.getClass.getClassLoader)
    } else if (flowType == "cang") {
      YingGraphJar.getClass.getClassLoader
    } else {
      throw new Exception(s"does not support $flowType")
    }
  }

  def loadGraph(flowType: String): FlowGraph = {
    import GraphConfigProtocol._
    import spray.json._

    val classLoader = getClassLoader(flowType)

    // get graph config
    val graphConfig = Source.fromInputStream(classLoader.getResourceAsStream("flow.json"))
      .mkString
      .parseJson
      .convertTo[GraphConfig]

    println(graphConfig.toJson.prettyPrint)

    // graphJar class and graphJar object
    val module = classLoader.loadClass(graphConfig.graphJar + "$")
    val graphJar = module.getField("MODULE$").get(null)

    // auto auto actor behavior from graphJar
    val autoMap = getAutoMap(module.getClass)

    // deciMap from graphJar
    val deciMap = getDeciderMap(module.getClass)

    // graph intial vertex
    val initial = graphConfig.initial

    // 返回流程
    new FlowGraph {
      override def getFlowGraph(state: State): Graph = Graph(graphConfig.edges, state, graphConfig.poinsts)

      override def getFlowInitial: String = initial

      override def getFlowType: String = flowType

      override def getUserTask: Map[String, Array[String]] = graphConfig.userTasks

      override def getAutoTask: Map[String, Array[String]] = graphConfig.autoTasks

      override def getEdges: Map[String, Edge] = graphConfig.edges.map(entry =>
        (entry._1, Edge(
          entry._1,
          entry._2.autoTasks,
          entry._2.userTasks,
          entry._2.partUTasks,
          entry._2.partGTasks
        ))
      )

      // new approach
      override def getAutoMeth: Map[String, Method] = autoMap

      override def getDeciMeth: Map[String, Method] = deciMap

      override def getGraphJar: AnyRef = graphJar
    }
  }


  def getAutoMap(m: Class[_]) = {
    m.getMethods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[CommandAutoTask] &&
        m.getReturnType == classOf[Future[Map[String, String]]]
    }.map { am =>
      (am.getName -> am)
    }.toMap
  }

  def getDeciderMap(m: Class[_]) = {
    m.getMethods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[State] &&
        m.getReturnType == classOf[Arrow]
    }.map { am =>
      (am.getName -> am)
    }.toMap
  }


  // 测试, 先看能否动态加载
  def kload: FlowGraph = {
    val name = "com.yimei.cflow.graph.ying2.YingGraph$"
    val module = this.getClass.getClassLoader.loadClass(name)
    try {
      module.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
    } catch {
      case e: java.lang.ClassCastException =>
        printf(" - %s is not Module\n", module)
        throw e
    }
  }
}
