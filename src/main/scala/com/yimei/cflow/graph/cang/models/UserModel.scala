package com.yimei.cflow.graph.cang.models

import java.time.LocalDateTime

/** 添加用户 **/
case class AddUser(username: String, password: String, name: String, email: String, phone: String, companyName: String, roleId: Int)

/** 修改用户 **/
case class UpdateUser(username: String, name: String, email: String, phone: String, companyName: String, roleId: Int)

/** 用户登陆 **/
case class UserLogin(username: String, password: String)

/** 用户自己修改密码 **/
case class UserResetPwd(newPassword: String, oldPassword: String)

/** 用户管理,列表字段 **/
case class UserList(username: String, name: String, companyName: String, roleName: String, lastLoginTime: LocalDateTime)

/** 用户管理-列表, 搜索 **/
case class UserListSearch(username: String, name: String, companyName: String, roleId: Int)

/** 管理员重置密码 **/
case class AdminResetUserPwd(id: BigInt)






