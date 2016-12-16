package com.yimei.cflow.graph.cang.viewModels

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import com.yimei.cflow.graph.cang.models.BaseFormatter._

object UserView extends DefaultJsonProtocol {

  implicit val userListFormat = jsonFormat6(UserList)
  implicit val userDetailFormat = jsonFormat8(UserDetail)

  /** 用户管理,列表字段 **/
  case class UserList(id: Int, username: String, name: String, companyName: String, roleName: String, createTim: Timestamp)

  /** 用户详细信息 **/
  case class UserDetail(id: BigInt, username: String, name: String, email: String, phone: String, companyName: String, roleName: String, createTime: Timestamp)

}
