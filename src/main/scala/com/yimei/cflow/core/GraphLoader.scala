package com.yimei.cflow.core

import java.io.File
import java.lang.reflect.Constructor
import java.net.URLClassLoader
import java.util.Map.Entry
import java.util.function.Consumer

import akka.actor.{ActorRef, Props}
import com.typesafe.config.ConfigValue
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.{Arrow, Edge, Graph, State}
import com.yimei.cflow.core.FlowRegistry.AutoProperty

/**
  * Created by hary on 16/12/17.
  */
object GraphLoader extends CoreConfig {

  // 加载所有
  def loadall(): Unit = {
    val flows = coreConfig.getConfig("flow");
    flows.entrySet().forEach(new Consumer[Entry[String, ConfigValue]] {
      override def accept(t: Entry[String, ConfigValue]): Unit = {

        val jarFile: String = null // todo:  get from config
        val graph = loadGraph(t.getKey, jarFile) // 加载

        FlowRegistry.register(t.getKey, graph) // 注册graph
      }
    })
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

  def loadGraph(flowType: String, jarFile: String): FlowGraph = {

    // 加载器
    val classLoader: URLClassLoader = new java.net.URLClassLoader(Array(new File(jarFile).toURI.toURL),
      this.getClass.getClassLoader)

    // 加载决策器
    val deciders = loadDeciders(classLoader)

    // 加载自动任务Actor
    val actors = loadActors(classLoader) // AutoActor的class名字为配置文件提供, 所以是可以读出来的

    // 从配置文件中读取各项配置
    val userTasks: Map[String, Array[String]] = null;

    //
    val initial = ???

    val userTask = ???

    val graph: Graph = ???

    // 返回流程
    new FlowGraph {
      override def getFlowGraph(state: State): Graph = graph

      override def getAutoTask: Map[String, AutoProperty] = actors

      override def getFlowInitial: String = initial

      override def getFlowType: String = flowType

      override def getUserTask: Map[String, Array[String]] = userTask

      override def getDeciders: Map[String, (State) => Arrow] = deciders

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
