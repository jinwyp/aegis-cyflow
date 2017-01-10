package com.yimei.cflow.engine.util.module

import akka.actor.Actor

/**
  * 可服务的行为
  */
trait ServicableBehavior {
  def serving: Actor.Receive
}
