package com.yimei.cflow.graph.cang.services

import com.yimei.cflow.api.http.client.PartyClient


/**
  * Created by xl on 16/12/26.
  */
object LoginService extends PartyClient{
  val PARTY_CLASS = "rzf"

  def financeSideEnter(userId: String, companyId: String, compayName: String): String = {
    val cn = queryPartyInstance(PARTY_CLASS, companyId)
    cn foreach {
      case a: String => println(a)
    }
    "success"
  }

}
