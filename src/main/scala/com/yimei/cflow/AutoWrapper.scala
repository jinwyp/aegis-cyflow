package com.yimei.cflow

import java.io.File
import java.lang.reflect.{Constructor, Method}
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.Flow.{Arrow, CommandPoints, DataPoint, State}
import com.yimei.cflow.core.FlowGraph

import scala.concurrent.Future

case class AutoPoint(value: String)

class AutoActor(
                 name: String,
                 modules: Map[String, ActorRef],
                 auto: CommandAutoTask => Future[Map[String, String]]
               )
  extends Actor
    with ActorLogging {

  import scala.concurrent.ExecutionContext
  import ExecutionContext.Implicits.global

  override def receive: Receive = {
    case task: CommandAutoTask =>
      auto(task).map { values =>
        modules(module_flow) ! CommandPoints(
          task.flowId,
          values.map { entry =>
            ((entry._1) -> DataPoint(entry._2, None, Some(name), UUID.randomUUID().toString, 10, false))
          }
        )
      }
  }
}

/**
  * Created by hary on 16/12/18.
  */
object AutoWrapper extends App {

  val autos = Map(
    "auto_A" -> autoA _,
    "auto_B" -> autoA _,
    "auto_C" -> autoA _,
    "auto_D" -> autoA _
  );

  def autoA(autoTask: CommandAutoTask): Future[Map[String, String]] = {
    val state: State = null // todo should come from autoTask
    Future {
      Map("hello" -> "world")
    }
  }

  def v0(state: State): Arrow = {
    println("hello")
    Arrow("v1", Some("edge"))
  }

  val autoMap = getAutoMap(AutoWrapper.getClass)
  val deciMap: Map[String, Method] = getDeciderMap(AutoWrapper.getClass)

  println(deciMap("v0").invoke(this, null))

  val ff: Future[Map[String, String]] =
    autoMap("autoA").invoke(this, null).asInstanceOf[Future[Map[String, String]]]

  ff.onSuccess {
    case m => println(m)
  }

  Thread.sleep(1000)

  System.exit(0)


  var classLoader = new java.net.URLClassLoader(Array(new File("module.jar").toURI.toURL),
    this.getClass.getClassLoader)

  val name = "com.yimei.cflow.graph.ying.YingGraph$"
  val module: Class[_] = this.getClass.getClassLoader.loadClass(name)
  try {
    module.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
  } catch {
    case e: java.lang.ClassCastException =>
      printf(" - %s is not Module\n", module)
      throw e
  }

  def getAutoMap(m: Class[_]) = {

    val methods: Array[Method] = m.getMethods
    val parameterTypes = classOf[CommandAutoTask]

    // 所有的 autoMethod
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
    // 所有的 autoMethod
    methods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[State] &&
        m.getReturnType == classOf[Arrow]
    }.map { am =>
      (am.getName -> am)
    }.toMap


  }

  // 加载actor
  // 获取构造器
  val y: Class[_] = classLoader.loadClass("com.yimei.cflow.graph.ying.YingGraph$AutoA")
  val constructor: Constructor[_] = y.getConstructor(classOf[Map[String, ActorRef]])
  val k = constructor.asInstanceOf[Constructor[AutoActor]]
  val prop = (modules: Map[String, ActorRef]) => Props(k.newInstance(modules))
}
