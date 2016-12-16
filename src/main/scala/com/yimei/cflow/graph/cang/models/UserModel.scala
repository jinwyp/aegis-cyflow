package com.yimei.cflow.graph.cang.models

import java.time.LocalDateTime

/** 添加用户 **/
case class AddUser(username: String, password: String, name: String, email: String, phone: String, companyName: String, roleId: Int)

/** 管理员修改用户 **/
case class UpdateUser(id: BigInt, username: String, name: String, email: String, phone: String, companyName: String, roleId: Int)

/** 用户修改自己信息 **/
case class UpdateSelf(email: String, phone: String)

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

/** 管理员禁用用户 **/
case class AdminDisableUser(id: BigInt)

/** 用户详细信息 **/
case class UserDetail(id: BigInt, username: String, name: String, email: String, phone: String, companyName: String, roleName: String, lastLoginTime: LocalDateTime)

/** 用户登陆日志信息 **/
case class UserLoginLog(userId: BigInt, companyId: BigInt, username: String, createTime: LocalDateTime)








