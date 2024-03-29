package com.yimei.cflow.test

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.DaemonMaster
import com.yimei.cflow.id.IdBufferable

/**
  * Created by hary on 16/12/16.
  */
object IdGeneratorTest extends App {
  val names = Array(module_id)

  // daemon master and
  val daemon = coreSystem.actorOf(DaemonMaster.props(names), "DaemonMaster")
  val proxy = coreSystem.actorOf(ServiceProxy.props(daemon, names), "ServiceProxy")
  Thread.sleep(1000)

  coreSystem.actorOf(Props(new IdBufferTest(proxy)), "idbufferTeset")
}

class IdBufferTest(proxy: ActorRef) extends Actor with IdBufferable {

  import scala.concurrent.duration._

  override val bufferSize: Int = 10
  override val bufferKey: String = "hello"

  implicit val myIdGenerator = proxy
//  implicit val myEc = coreExecutor
//  implicit val myTimeout = coreTimeout

  context.system.scheduler.schedule(1 seconds, 1 seconds, self, 1)

  override def receive: Receive = {
    case 1 =>
      val id = nextId
      println(s"id = $id")
  }
}
