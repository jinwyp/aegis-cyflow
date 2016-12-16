package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import BaseFormatter._;

object UserModel extends DefaultJsonProtocol {

  /** 添加用户 **/
  case class AddUser(username: String, password: String, name: String, email: String, phone: Option[String], companyName: String, roleId: Int)
  implicit val addUserFormat = jsonFormat7(AddUser)

  /** 管理员修改用户 **/
  case class UpdateUser(id: BigInt, username: String, name: String, email: String, phone: Option[String], companyName: String, roleId: Int)
  implicit val updateUserFormat = jsonFormat7(UpdateUser)

  /** 用户修改自己信息 **/
  case class UpdateSelf(email: String, phone: Option[String])
  implicit val updateSelfFormat = jsonFormat2(UpdateSelf)

  /** 用户登陆 **/
  case class UserLogin(username: String, password: String, lastUpdateTime: Option[Timestamp])
  implicit val userLoginFormat = jsonFormat3(UserLogin)

  /** 用户自己修改密码 **/
  case class UserChangePwd(newPassword: String, oldPassword: String)
  implicit val userChangePwdFormat = jsonFormat2(UserChangePwd)

  /** 用户管理-列表, 搜索参数 **/
  case class UserListSearch(username: String, name: String, companyName: String, roleId: Int)
  implicit val userListSearchFormat = jsonFormat4(UserListSearch)

  /** 管理员重置密码 **/
  case class AdminResetUserPwd(id: BigInt)
  implicit val adminResetUserPwdFormat = jsonFormat1(AdminResetUserPwd)

  /** 管理员禁用用户 **/
  case class AdminDisableUser(id: BigInt)
  implicit val adminDisableUserFormat = jsonFormat1(AdminDisableUser)


}








