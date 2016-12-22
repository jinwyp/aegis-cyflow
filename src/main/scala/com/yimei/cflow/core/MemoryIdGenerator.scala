package com.yimei.cflow.core

import com.yimei.cflow.api.models.id.{AbstractIdGenerator, CommandGetId, EventIncrease, Id}


/**
  * Created by hary on 16/12/9.
  */
class MemoryIdGenerator(name: String) extends AbstractIdGenerator {

  import IdGenerator._

  override def receive: Receive = commonBehavior orElse serving

  def serving: Receive = {
    case CommandGetId(key, buffer) =>
      val old = updateState(EventIncrease(key, buffer))
      sender() ! Id(old + 1)
  }
}


