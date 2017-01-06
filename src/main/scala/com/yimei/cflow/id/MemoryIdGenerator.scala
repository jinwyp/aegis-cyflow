package com.yimei.cflow.id

import com.yimei.cflow.api.models.id.{CommandGetId, EventIncrease, Id}


/**
  * Created by hary on 16/12/9.
  */
class MemoryIdGenerator(name: String) extends AbstractIdGenerator {

  override def receive: Receive = commonBehavior orElse serving

  def serving: Receive = {
    case CommandGetId(key, buffer) =>
      val old = updateState(EventIncrease(key, buffer))
      sender() ! Id(old + 1)
  }
}


