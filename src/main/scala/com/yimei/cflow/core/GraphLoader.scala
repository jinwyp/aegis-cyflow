package com.yimei.cflow.core

import java.io.{File, InputStream}
import java.lang.reflect.Constructor
import java.net.URLClassLoader

import akka.actor.{ActorRef, Props}
import com.typesafe.config.{Config, ConfigFactory}
import com.yimei.cflow.core.Flow.{Arrow, Edge, Graph, State}
import com.yimei.cflow.core.FlowRegistry.AutoProperty

/**
  * Created by hary on 16/12/17.
  */
object GraphLoader {

  // 加载所有
  def loadall(): Unit = {

    // 1> list directories of flows
    val graphs: Array[String] = ???

    // 2>
    graphs.foreach(loadGraph(_))
  }


  // 加载deciders
  def loadDeciders(classLoader: ClassLoader): Map[String, State => Arrow] = ???

  // 加载actors
  def loadActors(classLoader: ClassLoader) : Map[String, AutoProperty] = ???

  // 加载actor
  def loadActor(classLoader: ClassLoader): AutoProperty = {

    // 加载actor
    val y: Class[_] = classLoader.loadClass("com.yimei.cflow.graph.ying.YingGraph$AutoA")

    // 获取构造器
    val constructor: Constructor[_] = y.getConstructor(classOf[Map[String, ActorRef]])
    val k = constructor.asInstanceOf[Constructor[AutoActor]]

    //
    val prop = (modules: Map[String, ActorRef]) => Props(k.newInstance(modules))

    AutoProperty(Array("a", "b"), prop)
  }

  def loadGraph(flowType: String): FlowGraph = {

    // 1> list flows/$flowType/*.jar
    val jars: Array[String] = ???

    // 2> create classloader for this graph
    val classLoader: URLClassLoader = new java.net.URLClassLoader(jars.map(new File(_).toURI.toURL), this.getClass.getClassLoader)

    // 3> 读取type
    val config: Config = ConfigFactory.load(classLoader, "flow")

    // 4> 拿到graphJar对象
    val graphJarName: String = ???
    val module = classLoader.loadClass(graphJarName + "$")
    val graphJar = module.getField("MODULE$").get(null).asInstanceOf[GraphJar]

    // 拿到deciders

    // 拿到autoProperties


    val initial = ???

    val userTask = ???

    val graph: Graph = ???

    // 返回流程
    new FlowGraph {
      override def getFlowGraph(state: State): Graph = graph

      override def getAutoTask: Map[String, AutoProperty] = ???

      override def getFlowInitial: String = initial

      override def getFlowType: String = flowType

      override def getUserTask: Map[String, Array[String]] = userTask

      override def getDeciders: Map[String, (State) => Arrow] = ???

      override def getEdges: Map[String, Edge] = ???
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
