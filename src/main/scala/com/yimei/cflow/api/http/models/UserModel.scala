package com.yimei.cflow.api.http.models

import com.yimei.cflow.api.models.user.{State, UserProtocol}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._
import spray.json.DefaultJsonProtocol

/**
  * Created by xl on 16/12/23.
  */
object UserModel {

  case class UserInfo(password:String, phone:Option[String],email:Option[String], name:String)

  case class QueryUserResult(userInfo:PartyUserEntity,status:State)

  case class UserListEntity(userList:Seq[PartyUserEntity],total:Int)

  trait UserModelProtocol extends DefaultJsonProtocol with UserProtocol {

    implicit val addUserModelFormat = jsonFormat4(UserInfo)
    implicit val queryUserResult = jsonFormat2(QueryUserResult)
    implicit val userlistFormat = jsonFormat2(UserListEntity)
  }
}
