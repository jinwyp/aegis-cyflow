package com.yimei.cflow.graph.cang.viewModels

import spray.json.DefaultJsonProtocol


object UserView extends DefaultJsonProtocol {

  implicit val userListFormat = jsonFormat5(UserList)
  implicit val userDetailFormat = jsonFormat7(UserDetail)

  /** 用户管理,列表字段 **/
  case class UserList(id: Int, username: String, name: String, companyName: String, roleName: String)

  /** 用户详细信息 **/
  case class UserDetail(id: BigInt, username: String, name: String, email: String, phone: String, companyName: String, roleName: String)

}
