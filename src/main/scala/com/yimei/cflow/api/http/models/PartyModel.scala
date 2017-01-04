package com.yimei.cflow.api.http.models

import com.yimei.cflow.api.models.database.UserOrganizationDBModel.PartyInstanceEntity
import com.yimei.cflow.api.models.user.UserProtocol
import spray.json.DefaultJsonProtocol

/**
  * Created by xl on 16/12/29.
  */
object PartyModel {
  case class PartyInstanceInfo(party: String, instanceId: String, companyName: String)

  case class PartyInstanceListEntity(partyInstanceList: Seq[PartyInstanceEntity], total: Int)

  trait PartyModelProtocal extends DefaultJsonProtocol with UserProtocol{
    implicit val PartyInstanceInfoFormat = jsonFormat3(PartyInstanceInfo)
    implicit val partyInstanceListEntityFormat = jsonFormat2(PartyInstanceListEntity)
  }
}
