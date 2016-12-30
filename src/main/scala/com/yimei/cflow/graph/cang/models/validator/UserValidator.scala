package com.yimei.cflow.graph.cang.models.validator

import com.wix.accord.Validator
import com.wix.accord.dsl._
import com.yimei.cflow.graph.cang.models.UserModel.{AddUser, AdminDisableUser, AdminResetUserPwd, UpdateSelf, UpdateUser, UserChangePwd, UserListSearch, UserLogin}


object UserValidator {

  /** 添加用户 **/
  implicit val addUserValidator: Validator[AddUser] =
    validator[AddUser] {
      addUser =>
        addUser.className as "用户类型" is notEmpty
        addUser.className.length as "用户类型字段长度" is between(1, 10)
        addUser.username as "登录名" is notEmpty
        addUser.username.length as "登录名字段长度" is between(2, 20)
        addUser.password as "密码" is notEmpty
        addUser.password.length as "密码字段长度" is between(6, 16)
        addUser.name as "姓名" is notEmpty
        addUser.name.length as "姓名字段长度" is between(1, 10)
        addUser.email as "邮箱" is notEmpty
        addUser.email.length as "邮箱字段长度" is between(1, 100)
        addUser.phone.length as "手机号字段长度" is between(11, 20)
        addUser.companyName as "公司名称" is notEmpty
        addUser.companyName.length as "公司名称字段长度" is between(1, 100)
    }

  /** 管理员修改用户信息 **/
  implicit val updateUserValidator: Validator[UpdateUser] =
    validator[UpdateUser] {
      updateUser =>
        updateUser.id as "用户id" min(1)
//        updateUser.className as "用户类型" is notEmpty
//        updateUser.className.length as "用户类型字段长度" is between(1, 10)
        updateUser.username as "登录名" is notEmpty
        updateUser.username.length as "登录名字段长度" is between(2, 20)
        updateUser.name as "姓名" is notEmpty
        updateUser.name.length as "姓名字段长度" is between(1, 10)
        updateUser.email as "邮箱" is notEmpty
        updateUser.email.length as "邮箱字段长度" is between(1, 100)
        updateUser.phone.length as "手机号字段长度" is between(11, 20)
//        updateUser.companyName as "公司名称" is notEmpty
//        updateUser.companyName.length as "公司名称字段长度" is between(1, 100)
    }

  /** 用户修改自己信息 **/
  implicit val updateSelfValidator: Validator[UpdateSelf] =
    validator[UpdateSelf] {
      updateSelf =>
        updateSelf.email as "邮箱" is notEmpty
        updateSelf.email.length as "邮箱字段长度" is between(1, 100)
        updateSelf.phone.length as "手机号字段长度" is between(11, 20)
    }

  /** 用户登陆 **/
  implicit val userLoginValidator: Validator[UserLogin] =
    validator[UserLogin] {
      userLogin =>
        userLogin.username as "登录名" is notEmpty
        userLogin.username.length as "登录名字段长度" is between(2, 20)
        userLogin.password as "密码" is notEmpty
        userLogin.password.length as "密码字段长度" is between(6, 16)
    }

  /** 用户自己修改密码 **/
  implicit val userChangePwdValidator: Validator[UserChangePwd] =
    validator[UserChangePwd] {
      userChangePwd =>
        userChangePwd.oldPassword as "原密码" is notEmpty
        userChangePwd.oldPassword.length as "原密码字段长度" is between(6, 16)
        userChangePwd.newPassword as "新密码" is notEmpty
        userChangePwd.newPassword.length as "新密码字段长度" is between(6, 16)
    }

  /** 用户管理-列表, 搜索参数 **/
  implicit val userListSearchValidator: Validator[UserListSearch] =
    validator[UserListSearch] {
      userListSearch =>
        userListSearch.className.length as "用户类型字段长度" is between(1, 10)
        userListSearch.username.length as "登录名字段长度" is between(2, 20)
        userListSearch.name.length as "姓名字段长度" is between(1, 10)
        userListSearch.companyName.length as "公司名称字段长度" is between(1, 100)
    }

  /** 管理员重置密码 **/
  implicit val adminResetUserPwdValidator: Validator[AdminResetUserPwd] =
    validator[AdminResetUserPwd] {
      adminResetUserPwd =>
        adminResetUserPwd.id as "用户id" min(1)
    }

  /** 管理员禁用用户 **/
  implicit val adminDisableUserValidator: Validator[AdminDisableUser] =
    validator[AdminDisableUser] {
      adminDisableUser =>
        adminDisableUser.id as "用户id" min(1)
    }

}
