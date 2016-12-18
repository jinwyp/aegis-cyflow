package com.yimei.cflow

import java.io.File
import java.lang.reflect.{Constructor, Method}
import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.{AutoActor, FlowGraph}
import com.yimei.cflow.core.Flow.{Arrow, CommandPoints, DataPoint, State}

import scala.concurrent.Future

case class AutoPoint(value: String)

class AutoActor(
                 name: String,
                 modules: Map[String, ActorRef],
                 auto: CommandAutoTask => Future[Map[String, String]]
               )
  extends Actor
    with ActorLogging {

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


  var classLoader = new java.net.URLClassLoader(Array(new File("module.jar").toURI.toURL),
    this.getClass.getClassLoader)

  val name = "com.yimei.cflow.graph.ying.YingGraph$"
  val module = this.getClass.getClassLoader.loadClass(name)
  try {
    module.getField("MODULE$").get(null).asInstanceOf[FlowGraph]
  } catch {
    case e: java.lang.ClassCastException =>
      printf(" - %s is not Module\n", module)
      throw e
  }

  def getAutoMap = {

    val methods: Array[Method] = module.getMethods
    val parameterTypes = classOf[CommandAutoTask]
    // 所有的 autoMethod
    val autoMap: Map[String, Method] = methods.filter { m =>
      val ptypes = m.getParameterTypes
      ptypes.length == 1 &&
        ptypes(0) == classOf[CommandAutoTask] &&
      m.getReturnType == classOf[Map[String,String]]
    }.map { am =>
      (am.getName -> am)
    }.toMap

  }

  def getDeciderMap = {
    val methods: Array[Method] = module.getMethods
    val parameterTypes = classOf[CommandAutoTask]
    // 所有的 autoMethod
    val autoMap: Map[String, Method] = methods.filter { m =>
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
