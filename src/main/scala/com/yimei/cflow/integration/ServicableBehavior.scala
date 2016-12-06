package com.yimei.cflow.integration

import akka.actor.Actor

/**
  * 可服务的行为
  */
trait ServicableBehavior {
  def serving: Actor.Receive
}
