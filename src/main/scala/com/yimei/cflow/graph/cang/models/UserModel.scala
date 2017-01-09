package com.yimei.cflow.graph.cang.models

import java.sql.Timestamp

import spray.json.DefaultJsonProtocol
import BaseFormatter._;

object UserModel extends DefaultJsonProtocol {

  /** 添加用户 **/
  case class AddUser(username: String, password: String, name: String, email: String, phone: String, instanceId: String, className: String)
  implicit val addUserFormat = jsonFormat7(AddUser)

  /** 管理员修改用户 **/
  case class UpdateUser(userid: String, username: String, password: String, name: String, email: String, phone: String)
  implicit val updateUserFormat = jsonFormat6(UpdateUser)

  /** 用户修改自己信息 **/
  case class UpdateSelf(email: String, phone: String)
  implicit val updateSelfFormat = jsonFormat2(UpdateSelf)

  /** 用户登陆 **/
  case class UserLogin(username: String, password: String, lastUpdateTime: Option[Timestamp])
  implicit val userLoginFormat = jsonFormat3(UserLogin)

  /** 用户自己修改密码 **/
  case class UserChangePwd(newPassword: String, oldPassword: String)
  implicit val userChangePwdFormat = jsonFormat2(UserChangePwd)

  /** 用户管理-列表, 搜索参数 **/
  case class UserListSearch(username: Option[String], name: Option[String], companyName: Option[String], className: Option[String])
  implicit val userListSearchFormat = jsonFormat4(UserListSearch)

  /** 管理员重置密码 **/
  case class AdminResetUserPwd(id: Long)
  implicit val adminResetUserPwdFormat = jsonFormat1(AdminResetUserPwd)

  /** 管理员禁用用户 **/
  case class AdminDisableUser(id: Long)
  implicit val adminDisableUserFormat = jsonFormat1(AdminDisableUser)

  case class AddCompany(companyName: String, partyClass: String)
  implicit val addCompanyFormat = jsonFormat2(AddCompany)

//  case class UpdateCompany(companyName: String, partyClass: String)
//  implicit val updateCompanyFormat = jsonFormat2(UpdateCompany)

  //-------------------------------------返回model----------------------------------
  //登录返回信息
  case class UserData(userId: String, username: String, email: String, phone: String, role: String, companyId: String, companyName: String)
  implicit val dataFormat = jsonFormat7(UserData)
  case class UserInfoList(datas: List[UserData], total: Int)
  implicit val UserInfoListFormat = jsonFormat2(UserInfoList)

}








