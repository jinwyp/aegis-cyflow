package com.yimei.cflow.core

import java.io.File
import java.net.URLClassLoader
import java.util
import java.util.Map.Entry
import java.util.function.Consumer

import akka.actor.{ActorRef, Props}
import com.typesafe.config.{Config, ConfigValue}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.core.Flow.{Graph, State}
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
        val graph = loadGraph(t.getKey, jarFile)  // 加载

        FlowRegistry.register(t.getKey, graph)  // 注册graph
      }
    })
  }

  // 加载deciders
  def loadDeciders(classLoader: ClassLoader) = {
  }

  // 加载actors
  def loadActors(classLoader: ClassLoader) = {

  }

  def loadGraph(flowType: String, jarFile: String): FlowGraph = {

    // 加载器
    val classLoader: URLClassLoader = new java.net.URLClassLoader(
      Array(new File(jarFile).toURI.toURL),
      this.getClass.getClassLoader)

    // 加载决策器
    loadDeciders(classLoader)

    // 加载自动任务Actor
    loadActors(classLoader)

    // 从配置文件中读取各项配置
    val userTasks: Map[String, Array[String]] = null;

    // 返回流程
    new FlowGraph {
      override def getFlowGraph(state: State): Graph = ???
      override def getAutoTask: Map[String, AutoProperty] = ???
      override def getFlowInitial: String = ???
      override def getFlowType: String = ???
      override def getUserTask: Map[String, Array[String]] = ???
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


  def load() = {

    /*
   * need to specify parent, so we have all class instances
   * in current context
   */
    var classLoader = new java.net.URLClassLoader(Array(new File("module.jar").toURI.toURL),
      this.getClass.getClassLoader)

    /*
   * please note that the suffix "$" is for Scala "object",
   * it's a trick
   */
    var clazzExModule = classLoader.loadClass("com.yimei.graph.ying.Ying" + "$")

    /*
   * currently, I don't know how to check if clazzExModule is instance of
   * Class[Module], because clazzExModule.isInstanceOf[Class[_]] always
   * returns true,
   * so I use try/catch
   */
    try {
      //"MODULE$" is a trick, and I'm not sure about "get(null)"
      var module = clazzExModule.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
    } catch {
      case e: java.lang.ClassCastException =>
        printf(" - %s is not Module\n", clazzExModule)
    }
  }
}