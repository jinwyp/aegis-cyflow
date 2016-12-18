package com.yimei.cflow.core

import java.io.File
import java.net.URLClassLoader
import java.util.Map.Entry
import java.util.function.Consumer

import com.typesafe.config.{Config, ConfigValue}
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.FlowRegistry.AutoProperty
import com.yimei.cflow.graph.ying2.YingGraph
import spray.json.DefaultJsonProtocol

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.io.Source

case class GraphConfig(
                     graphJar: String,
                     persistent: Boolean,
                     timeout: Int,
                     initial: String,
                     poinsts: Map[String, String],
                     autoTasks: Map[String, Array[String]],
                     userTasks: Map[String, Array[String]],
                     deciders: Map[String,String],
                     edges: Map[String, EdgeDescription]
                     )

object GraphConfigProtocol extends DefaultJsonProtocol with FlowProtocol {
  implicit val graphConfigProtocolFormat = jsonFormat9(GraphConfig)
}



/**
  * Created by hary on 16/12/17.
  */
object GraphLoader extends App {

  //loadall()

  // 加载所有
  def loadall() = {

    val root: File = new File("flows")


    // 1> list directories of flows
    val graphs: Array[String] = root.listFiles().filter(_.isDirectory()).map(_.getName)

    // 2>
    graphs.foreach(flowType => FlowRegistry.register(flowType, loadGraph(flowType)))
  }



  // 加载deciders
  def loadDeciders(classLoader: ClassLoader): Map[String, State => Arrow] = ???

  // 加载actors
  def loadActors(classLoader: ClassLoader): Map[String, AutoProperty] = ???



  def getStringArray(absoluteKey: String, rootConfig: Config) = {
    import scala.collection.JavaConversions._
    val buffer = ArrayBuffer[String]()
    rootConfig.getStringList(absoluteKey).forEach(new Consumer[String] {
      override def accept(t: String) = buffer.add(t)
    })
    buffer.toArray
  }


  def getPartUTask(absoluteKey: String, rootConfig: Config): List[PartUTask] = {
    import scala.collection.JavaConversions._
    val buffer = ListBuffer[PartUTask]()
    //val buffer = ArrayBuffer[String]()
    rootConfig.getConfig(absoluteKey).entrySet().forEach(new Consumer[Entry[String, ConfigValue]] {
      override def accept(t: Entry[String, ConfigValue]) = {
        buffer.add(PartUTask(t.getKey, getStringArray(absoluteKey + "." + t.getKey, rootConfig).toList))
      }
    })
    buffer.toList
  }

  def getPartGTask(absoluteKey: String, rootConfig: Config): List[PartGTask] = {
    import scala.collection.JavaConversions._
    val buffer = ListBuffer[PartGTask]()
    //val buffer = ArrayBuffer[String]()
    rootConfig.getConfig(absoluteKey).entrySet().forEach(new Consumer[Entry[String, ConfigValue]] {
      override def accept(t: Entry[String, ConfigValue]) = {
        buffer.add(PartGTask(t.getKey, getStringArray(absoluteKey + "." + t.getKey, rootConfig).toList))
      }
    })
    buffer.toList
  }


  def getEdgeDescription(edgeName: String, rootConfig: Config) = {
    EdgeDescription(
      getStringArray(s"$edgeName.autoTasks", rootConfig).toList,
      getStringArray(s"$edgeName.userTasks", rootConfig).toList,
      getPartUTask(s"$edgeName.partUTasks", rootConfig),
      getPartGTask(s"$edgeName.partGTasks", rootConfig),
      rootConfig.getString(s"$edgeName.begin"),
      rootConfig.getString(s"$edgeName.end")
    )
  }


  def loadGraph(flowType: String): FlowGraph = {
    import GraphConfigProtocol._
    import spray.json._

    // 1> list flows/$flowType/*.jar
    val jars: Array[String] = (new File("flows/" + flowType)).listFiles().filter(_.isFile()).map(_.getPath)

    // 2> create classloader for this graph
    val classLoader: URLClassLoader = new java.net.URLClassLoader(jars.map(new File(_).toURI.toURL), this.getClass.getClassLoader)

    val jsonStr = Source.fromInputStream(classLoader.getResourceAsStream("flow.json")).mkString

    val graphConfig = jsonStr.parseJson.convertTo[GraphConfig]

    println(graphConfig.toJson.prettyPrint)

    // 4> 拿到graphJar对象
    //val rootConf = config.getConfig("flow." + flowType)
    val graphJarName: String = graphConfig.graphJar
    val module = classLoader.loadClass(graphJarName + "$")
    val graphJar = module.getField("MODULE$").get(null).asInstanceOf[GraphJar]

    // 拿到deciders

    // 拿到autoProperties


    val initial = graphConfig.initial


    // 返回流程
    new FlowGraph {
      override def getFlowGraph(state: State): Graph = Graph(graphConfig.edges, state, graphConfig.poinsts)

      override def getAutoTask: Map[String, AutoProperty] = graphJar.getAutoProperties

      override def getFlowInitial: String = initial

      override def getFlowType: String = flowType

      override def getUserTask: Map[String, Array[String]] = graphConfig.userTasks

      override def getDeciders: Map[String, (State) => Arrow] = graphJar.getDeciders

      override def getEdges: Map[String, Edge] = graphConfig.edges.map(entry =>
            (entry._1, Edge(
              entry._1,
              entry._2.autoTasks,
              entry._2.userTasks,
              entry._2.partUTasks,
              entry._2.partGTasks
            ))
      )
    }
  }


  // 测试, 先看能否动态加载
  def kload: FlowGraph = {
    val name = "com.yimei.cflow.graph.ying.YingGraph$"
    val module = this.getClass.getClassLoader.loadClass(name)
    try {
      module.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
    } catch {
      case e: java.lang.ClassCastException =>
        printf(" - %s is not Module\n", module)
        throw e
    }
  }

  def getClassLoader() = {
    var classLoader = new java.net.URLClassLoader(Array(new File("module.jar").toURI.toURL),
      this.getClass.getClassLoader)
  }
}
