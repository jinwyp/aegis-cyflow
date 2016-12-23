package com.yimei.cflow.api.services

import akka.actor.Actor

/**
  * 可服务的行为
  */
trait ServicableBehavior {
  def serving: Actor.Receive
}
