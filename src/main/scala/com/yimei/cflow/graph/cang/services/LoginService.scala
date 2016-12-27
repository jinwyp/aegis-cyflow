package com.yimei.cflow.graph.cang.services

import akka.event.{Logging, LoggingAdapter}
import com.yimei.cflow.api.http.client.PartyClient
import com.yimei.cflow.api.util.HttpUtil._
import spray.json._
import DefaultJsonProtocol._

import scala.concurrent.Await
import scala.concurrent.duration.Duration


/**
  * Created by xl on 16/12/26.
  */
object LoginService extends PartyClient{
  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)
  val PARTY_CLASS = "rzf"

  case class PartyClassEntity(id:Option[Long],class_name:String,description:String)

  def financeSideEnter(userId: String, companyId: String, compayName: String): String = {
    log.info(s"get into financeSideEnter method: userId: ${userId}, companyId: ${companyId}, companyName: ${compayName}")
    val qpi = queryPartyInstance(PARTY_CLASS, companyId)

//    qpi match {
//      cd
//    }

//    Await.result(qpi, Duration.Inf)
//    qpi.value.get.get

//    println("result :" + qpi.value.get.get)
//    val pi = for {
//      pis <- qpi
//    } yield {
////      pis.parseJson.convertTo[PartyClassEntity]
//    }
//    if(pi != null) {
//      "success"
//    } else {
//      val cpi = createPartyInstance(PARTY_CLASS, companyId, compayName)
//      cpi
//      "success"
//    }
    "success"

  }

}
