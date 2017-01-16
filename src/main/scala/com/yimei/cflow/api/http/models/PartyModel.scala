package com.yimei.cflow.api.http.models

import com.yimei.cflow.api.models.database.UserOrganizationDBModel.PartyInstanceEntity
import com.yimei.cflow.api.models.user.UserProtocol
import com.yimei.cflow.graph.cang.models.DepositModel._
import spray.json.DefaultJsonProtocol

/**
  * Created by xl on 16/12/29.
  */
object PartyModel {
  case class PartyInstanceInfo(party: String, instanceId: String, companyName: String)

  case class PartyInstanceListEntity(partyInstanceList: Seq[PartyInstanceEntity], total: Int)
  case class CompanyInfoQueryResponse(success: Boolean, message: String)

  trait PartyModelProtocol extends DefaultJsonProtocol with UserProtocol{
    implicit val PartyInstanceInfoFormat = jsonFormat3(PartyInstanceInfo)
    implicit val partyInstanceListEntityFormat = jsonFormat2(PartyInstanceListEntity)
    implicit val CompanyInfoQueryResponseFormat = jsonFormat2(CompanyInfoQueryResponse)
  }
}
