package com.yimei.cflow.user

import com.yimei.cflow.user.User.{HierarchyInfo, State}
import com.yimei.cflow.user.UserMaster.GetUserData
import spray.json.DefaultJsonProtocol

/**
  * Created by hary on 16/12/6.
  */

trait UserProtocol extends DefaultJsonProtocol {

  implicit val userHierarchyInfoFormat = jsonFormat2 (HierarchyInfo)

  implicit val userGetUserDataFormat = jsonFormat3 (GetUserData)

  implicit val userStateFormat = jsonFormat2 (State)
}

