package com.yimei.cflow.user

import com.yimei.cflow.user.User.{State}
import com.yimei.cflow.user.UserMaster.CommandUserTask
import spray.json.DefaultJsonProtocol

/**
  * Created by hary on 16/12/6.
  */

trait UserProtocol extends DefaultJsonProtocol {

  implicit val userGetUserDataFormat = jsonFormat3(CommandUserTask)

  implicit val userStateFormat = jsonFormat3(State)
}

