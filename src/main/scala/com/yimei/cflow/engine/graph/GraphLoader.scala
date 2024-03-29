package com.yimei.cflow.engine.graph

import java.io.File

import akka.actor.ActorRef
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.models.auto.CommandAutoTask
import com.yimei.cflow.api.models.flow._
import com.yimei.cflow.api.models.graph.{GraphConfig, GraphConfigProtocol}
import com.yimei.cflow.engine.FlowRegistry
import com.yimei.cflow.graph.cang.CangGraphJar
import com.yimei.cflow.graph.money.MoneyGraphJar
import com.yimei.cflow.graph.ying.YingGraphJar

import scala.concurrent.Future
import scala.io.Source


/**
  * Created by hary on 16/12/17.
  */
object GraphLoader extends GraphConfigProtocol {

  def deploy(flowType: String) = {
    FlowRegistry.register(flowType, loadGraph(flowType, getClassLoader(flowType)))
  }

  def getClassLoader(flowType: String) = {
    flowType match {
      case "ying" => YingGraphJar.getClass.getClassLoader
      case "money" => MoneyGraphJar.getClass.getClassLoader
      case "cang" => CangGraphJar.getClass.getClassLoader
      case "zhou" =>
        // todo xj
        // 改为从数据库deploy表里读取jar文件 写入/tmp/$flowType.jar
        // 然后再  new java.net.URLClassLoader(new File("/tmp/xxx.jar").toURI.toURL), this.getClass.getClassLoader)
        null

      case _ => val jars: Array[String] = (new File("flows/" + flowType))
        .listFiles()
        .filter(_.isFile())
        .map(_.getPath)
        new java.net.URLClassLoader(jars.map(new File(_).toURI.toURL), this.getClass.getClassLoader)

    }
  }

  // todo 王琦
  // 改为从数据加载
  def loadFromDB() = {
    // select * from deploy where enabled = true
    // 对每个entry,   取出jar内容, 放入/tmp目录下, 然后拿到classLoader
    // graph = loadGraph(flowType, classLoader)
    // FlowRegistry.register(flowType, graph)
  }

  // 从目录加载
  def loadall() =
    new File("flows")
      .listFiles()
      .filter(_.isDirectory())
      .map { file =>
        val flowType = file.getName
        val classLoader = getClassLoader(flowType)

        val status = try {
          FlowRegistry.register(flowType, loadGraph(flowType, classLoader))
        } catch {
          case e: Throwable =>
            println(flowType + "!!!!!!!" + e.getMessage)

            false
        }
        (flowType, status)

      }.toMap

  def loadGraph(gFlowType: String, classLoader: ClassLoader): FlowGraph = {
    import spray.json._

    val jsonFile = gFlowType match {
      case "ying" => "ying.json"
      case "money" => "money.json"
      case "cang" => "cang.json"
      case _ => "flow.json"
    }

    var graphConfig = Source.fromInputStream(classLoader.getResourceAsStream(jsonFile))
      .mkString
      .parseJson
      .convertTo[GraphConfig]

    // todo need add start success and fail ?
    graphConfig = graphConfig.copy(edges = graphConfig.edges ++
      Map("start" -> Edge(name = "start", begin = "God", end = graphConfig.initial),
        "success" -> Edge(name = "success", end = "success"),
        "fail" -> Edge(name = "fail", end = "success")
      )
    )

    // graphJar class and graphJar object
    val mclass = classLoader.loadClass(s"${graphConfig.groupId}.${graphConfig.artifact}.${graphConfig.entry}" + "$")
    val graphJar = mclass.getField("MODULE$").get(null)


    val allDeciders = getDeciders(mclass, graphJar);
    val allAutos = getAutoMap(mclass, graphJar);

    // graph intial vertex
    val initial = graphConfig.initial

    val jarRoutes = getRoutes(mclass, graphJar)

    // 返回流程
    val g = new FlowGraph {

      override val timeout: Long = graphConfig.timeout

      override val persistent: Boolean = graphConfig.persistent

      override val points: Map[String, String] = graphConfig.points

      override val vertices: Map[String, String] = {
        graphConfig.vertices.map { entry =>
          (entry._1, entry._2.description)
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

      override val userTasks: Map[String, TaskInfo] = graphConfig.userTasks

      override val autoTasks: Map[String, TaskInfo] = graphConfig.autoTasks

      override val edges: Map[String, Edge] = graphConfig.edges

      override val pointEdges = pointEdgesImpl

      override val autoMethods: Map[String, CommandAutoTask => Future[Map[String, String]]] = allAutos

      override val deciders: Map[String, State => Seq[Arrow]] = allDeciders

      override val moduleJar: AnyRef = graphJar

      override val routes = jarRoutes
    }

    g
  }

  def getAutoMap(m: Class[_], module: AnyRef) = {
    m.getMethods.filter { method =>
      val ptypes = method.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[CommandAutoTask] &&
        method.getReturnType == classOf[Future[Map[String, String]]]
    }.map { am =>
      val behavior: CommandAutoTask => Future[Map[String, String]] =
        task => am.invoke(module, task).asInstanceOf[Future[Map[String, String]]]
      (am.getName -> behavior)
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

  def getRoutes(m: Class[_], module: AnyRef): Seq[ActorRef => Route] = {
    m.getMethods.filter { method =>
      val ptypes = method.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[ActorRef] &&
        method.getReturnType == classOf[Route]
    }.map { am =>
      (proxy: ActorRef) =>
        am.invoke(module, proxy).asInstanceOf[Route]
    }.toSeq
  }
}
