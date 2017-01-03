package com.yimei.cflow.engine.id

import akka.actor.Props

/**
  * Created by hary on 16/12/29.
  */
// create IdGenerator Props
object IdGenerator {
  def props(name: String, persist: Boolean = true) = persist match {

    //  import akka.actor.{Actor, ActorLogging}

    case true => Props(new PersistentIdGenerator(name))
    case false => Props(new MemoryIdGenerator(name))
  }
}