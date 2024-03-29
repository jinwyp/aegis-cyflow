package com.yimei.cflow.api.models.database

import java.sql.{Blob, Timestamp}

import spray.json.DefaultJsonProtocol

/**
  * Created by xl on 16/12/27.
  */
object FlowDBModel extends DefaultJsonProtocol {
  case class FlowInstanceEntity(id: Option[Long], flow_id: String, flow_type: String, user_type: String, user_id: String, data: String, state:String, finished: Int, ts_c: Timestamp)
  case class FlowTaskEntity(id: Option[Long], flow_id: String, task_id: String, task_name: String, task_submit: String, user_type: String, user_id: String, ts_c: Timestamp)
  case class DesignEntity(id: Option[Long], name: String, json: Option[String], meta: Option[String], ts_c: Option[Timestamp])
  case class DeployEntity(id: Option[Long], flow_type: String, jar: Blob, enable: Boolean, ts_c: Option[Timestamp])
}


object UserOrganizationDBModel {
  case class PartyClassEntity(id:Option[Long],class_name:String,description:String)
  case class PartyGroupEntity(id:Option[Long],party_class:String,gid:String,description:String,ts_c:Timestamp)
  case class PartyInstanceEntity(id:Option[Long], partyClass:String, instanceId:String, companyName:String, disable: Int, ts_c:Timestamp)
  case class PartyUserEntity(id:Option[Long], party_id:Long, user_id:String, password:String, phone:Option[String], email:Option[String], name:String, username:String, disable: Int, ts_c:Timestamp)
  case class UserGroupEntity(id: Option[Long], party_id: Long, gid: String, user_id: String, ts_c: Timestamp)
}

object AssetDBModel {
  case class AssetEntity(id:Option[Long], asset_id: String, file_type: Int, busi_type: String, username: String, gid: Option[String], description: Option[String], url: String, origin_name: String, ts_c: Option[Timestamp])
}
