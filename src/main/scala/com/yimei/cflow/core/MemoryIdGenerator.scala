package com.yimei.cflow.core
import akka.actor.Actor.Receive


/**
  * Created by hary on 16/12/9.
  */
class MemoryIdGenerator(name: String) extends AbstractIdGenerator {

  import IdGenerator._

  override def receive: Receive = commonBehavior orElse serving

  def serving: Receive = {
    case CommandGetId(key) =>
      updateState(EventIncrease(key))
      sender()! Id(state.keys(key) + 1)
  }
}


