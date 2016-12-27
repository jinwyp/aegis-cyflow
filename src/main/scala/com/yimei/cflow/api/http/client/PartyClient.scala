package com.yimei.cflow.api.http.client

import scala.concurrent.Future
import com.yimei.cflow.api.util.HttpUtil._
import akka.event.{Logging, LoggingAdapter}

/**
  * Created by hary on 16/12/23.
  */
trait PartyClient {
  def createPartyInstance(party_class: String, instance_id: String, party_name: String): Future[String] = {
    sendRequest(path = "api/inst", pathVariables = Array(party_class, instance_id, party_name), method = "post")
  }

  def queryPartyInstance(party_class: String, instance_id: String): Future[String] = {
    sendRequest(path = "api/inst", pathVariables = Array(party_class, instance_id), method = "get")
  }
}
