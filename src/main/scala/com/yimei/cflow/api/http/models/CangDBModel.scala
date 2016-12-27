package com.yimei.cflow.api.http.models

import java.sql.Timestamp

/**
  * Created by xl on 16/12/27.
  */
object CangDBModel {
  case class FlowInstanceEntity(id:Option[Long], flow_id:String, flow_type:String, user_type:String, user_id:String, state:String, finished:Int, ts_c:Timestamp)
  case class FlowTaskEntity(id:Option[Long], flow_id:String, task_id:String, task_name:String,task_submit:String, user_type:String, user_id:String, ts_c:Timestamp)
  case class PartyClassEntity(id:Option[Long],class_name:String,description:String)
  case class PartyGroupEntity(id:Option[Long],party_class:String,gid:String,description:String,ts_c:Timestamp)
  case class PartyInstanceEntity(id:Option[Long], party_class:String, instance_id:String, party_name:String, ts_c:Timestamp)
  case class PartyUserEntity(id:Option[Long], party_id:Long, user_id:String, password:String, phone:Option[String], email:Option[String], name:String, ts_c:Timestamp)
  case class UserGroupEntity(id: Option[Long], party_id: Long, gid: String, user_id: String, ts_c: Timestamp)
}
