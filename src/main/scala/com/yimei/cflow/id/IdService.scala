package com.yimei.cflow.id

import akka.actor.ActorRef
import com.yimei.cflow.id.models.{CommandGetId, CommandQueryId, Id, State}
import akka.pattern._
import akka.util.Timeout

/**
  * Created by hary on 17/1/6.
  */
trait IdService {

  def proxy: ActorRef;

  def idServiceTimeout: Timeout

  def idGet(key: String, buffer: Int = 1) = (proxy ? CommandGetId(key, buffer))(idServiceTimeout).mapTo[Id]

  def idState = (proxy ? CommandQueryId)(idServiceTimeout).mapTo[State]
}
