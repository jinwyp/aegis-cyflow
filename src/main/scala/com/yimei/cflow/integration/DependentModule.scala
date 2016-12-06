package com.yimei.cflow.integration

import akka.actor.ActorRef

/**
  * 依赖的模块
  */
trait DependentModule {
  def modules: Map[String, ActorRef]
}
