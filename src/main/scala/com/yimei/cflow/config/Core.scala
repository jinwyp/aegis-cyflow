package com.yimei.cflow.config

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import concurrent.duration._

/**
  * Created by hary on 16/12/2.
  */
trait Core {
  implicit val system = ActorSystem("RiskSystem")
  implicit val timeout = Timeout(5 seconds)
  implicit val executionContext = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val config = system.settings.config
}
