package com.yimei.cflow.test

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import com.yimei.cflow.api.services.{IdBufferable, ServiceProxy}
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.DaemonMaster

/**
  * Created by hary on 16/12/16.
  */
object IdGeneratorTest extends App with CoreConfig {
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
  implicit val myEc = context.system.dispatcher
  implicit val myTimeout = Timeout(3 seconds)

  context.system.scheduler.schedule(1 seconds, 1 seconds, self, 1)

  override def receive: Receive = {
    case 1 =>
      val id = nextId
      println(s"id = $id")
  }
}
