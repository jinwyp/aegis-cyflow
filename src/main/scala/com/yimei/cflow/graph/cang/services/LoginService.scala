package com.yimei.cflow.graph.cang.services

import akka.event.{Logging, LoggingAdapter}
import com.yimei.cflow.api.http.client.PartyClient
import com.yimei.cflow.api.util.HttpUtil._


/**
  * Created by xl on 16/12/26.
  */
object LoginService extends PartyClient{
  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)
  val PARTY_CLASS = "rzf"

  def financeSideEnter(userId: String, companyId: String, compayName: String): String = {
    log.info(s"get into financeSideEnter method: userId: ${userId}, companyId: ${companyId}, companyName: ${compayName}")
    val cn = queryPartyInstance(PARTY_CLASS, companyId)
    cn foreach {
      case a: String => println(a)
    }
    "success"
  }

}
