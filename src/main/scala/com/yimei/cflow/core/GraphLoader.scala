package com.yimei.cflow.core

import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader

import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.core.Flow._
import com.yimei.cflow.core.FlowRegistry.AutoProperty
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

  //loadall()

  // 加载所有
  def loadall() = {

    val root: File = new File("flows")


    // 1> list directories of flows
    val graphs: Array[String] = root.listFiles().filter(_.isDirectory()).map(_.getName)

    // 2>
    graphs.foreach(flowType => FlowRegistry.register(flowType, loadGraph(flowType)))
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

    val initial = graphConfig.initial

    val autoMap: Map[String, Method] = getAutoMap(module.getClass)
    val deciMap: Map[String, Method] = getDeciderMap(module.getClass)


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

      // new approach
      override def getAutoMap: Map[String, Method] = autoMap
      override def getDeciMap: Map[String, Method] = deciMap
      override def getGraphJar: AnyRef = module
    }
  }


  def getAutoMap(m: Class[_]) = {

    val methods: Array[Method] = m.getMethods
    val parameterTypes = classOf[CommandAutoTask]

    // 所有的 auto Method
    methods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[CommandAutoTask] &&
        m.getReturnType == classOf[Future[Map[String, String]]]
    }.map { am =>
      (am.getName -> am)
    }.toMap

  }

  def getDeciderMap(m: Class[_]) = {
    val methods: Array[Method] = m.getMethods
    val parameterTypes = classOf[State]
    // 所有的 Decider Method
    methods.filter { m =>
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
