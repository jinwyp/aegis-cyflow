package com.yimei.cflow.graph.cang.views

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import com.yimei.cflow.graph.cang.models.BaseFormatter._

object UserView extends DefaultJsonProtocol {

  /** 用户管理,列表字段 **/
  case class UserList(id: Long, username: String, name: String, companyName: String, className: String, createTim: Timestamp)
  implicit val userListFormat = jsonFormat6(UserList)

  /** 用户详细信息 **/
  case class UserDetail(id: Long, username: String, name: String, email: String, phone: String, companyName: String, className: String, createTime: Timestamp)
  implicit val userDetailFormat = jsonFormat8(UserDetail)

}
