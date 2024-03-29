package com.yimei.cflow.id

import akka.actor.ActorRef
import akka.pattern._
import akka.util.Timeout
import com.yimei.cflow.id.models.{CommandGetId, Id}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import com.yimei.cflow.config.CoreConfig._

/**
  * Created by hary on 16/12/16.
  */
trait IdBufferable {

  val bufferSize: Int   // need overriede
  val bufferKey: String

  var curId: Long =  0
  var max:Long  = 0;

//  implicit val myEc: ExecutionContext
//  implicit val myTimeout: Timeout
  def myIdGenerator: ActorRef

  def nextId = {
    if(curId == 0 || curId == max) {
      getBuffer
    }
    val ret = curId;
    curId = curId + 1
    ret
  }


  private def getBuffer() = {
    val fpid = (myIdGenerator ? CommandGetId(bufferKey, bufferSize)).mapTo[Id]
    curId = Await.result(fpid, 2 seconds).id
    max = curId + bufferSize;
  }
}
