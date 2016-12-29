package com.yimei.cflow.api.http.models

import spray.json.DefaultJsonProtocol

/**
  * Created by xl on 16/12/29.
  */
object PartyModel {
  case class PartyInstanceInfo(party: String, instanceId: String, companyName: String)

  trait PartyModelProtocal extends DefaultJsonProtocol {
    implicit val PartyInstanceInfoFormat = jsonFormat3(PartyInstanceInfo)
  }
}
