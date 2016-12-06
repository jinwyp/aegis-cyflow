package com.yimei.cflow.integration

import akka.actor.{ActorRef, Props}

/**
  * Created by hary on 16/12/6.
  */

object ServiceProxy {
  def props(daemon: ActorRef, modules: Array[String]) = Props(new ServiceProxy(daemon, modules))
}

/**
  * Created by hary on 16/12/6.
  */
class ServiceProxy(daemon: ActorRef, dependOn: Array[String]) extends ModuleMaster("serviceProxy", dependOn) with ServicableBehavior {

  // 服务!!!!
  override def serving: Receive = {


    case _ =>
  }

}
