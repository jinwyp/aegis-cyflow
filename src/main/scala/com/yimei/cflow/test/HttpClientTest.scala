package com.yimei.cflow.test

import java.util.UUID

import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.actor.{Actor, ActorLogging}
import akka.util.ByteString
import com.yimei.cflow.config.CoreConfig
import akka.pattern._
import com.yimei.cflow.api.models.flow.DataPoint
import com.yimei.cflow.http._

import scala.concurrent.Future

/**
  * Created by wangqi on 16/12/21.
  */
//class HttpClientTest extends Actor with ActorLogging with CoreConfig{
//
//  import akka.pattern.pipe
//  import context.dispatcher
//
//  val http = Http(context.system)
//  val url = "http://localhost:9000"
//
//
//  override def receive: Receive = {
//    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
//      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
//        log.info("Got response, body: " + body.utf8String)
//      }
//    case resp @ HttpResponse(code, _, _, _) =>
//      log.info("Request failed, response code: " + code)
//      resp.discardEntityBytes()
//  }
//}
import spray.json._

object ClientMain extends App with AdminProtocol with TaskProtocol with UserModelProtocol{
  //println(Map[String,String]("wang"->"qi","wang2"->"qi2").toJson.prettyPrint)

  //注意：此处调用方应该使用wrap
  //println(UserSubmitEntity("ying!rz-1!haryId1!1","TKPU1",Map("KPU1"->DataPoint("zj-1!wangqiId1",None,Some("wang"),UUID.randomUUID().toString, 0L, false))).toJson.prettyPrint)

 // println(UserSubmitEntity("ying!rz-1!haryId1!1","PU",Map(("PU1"->DataPoint("50",None,Some("wang"),UUID.randomUUID().toString, 0L, false)),
                                                           // ("PU2"->DataPoint("50",None,Some("wang"),UUID.randomUUID().toString, 0L, false)))).toJson.prettyPrint)

  println(Map("LoanReceipt"->UserSubmitMap(Some("pdf"),"http://www.pdf995.com/samples/pdf.pdf")).toJson.prettyPrint)

}