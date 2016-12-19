package com.yimei.cflow.user

import com.yimei.cflow.user.User.{CommandUserTask, State}
import com.yimei.cflow.user.db.PartyClassEntity
import spray.json.DefaultJsonProtocol

/**
  * Created by hary on 16/12/6.
  */

trait UserProtocol extends DefaultJsonProtocol {

  implicit val userCommandUserTaskFormat = jsonFormat4(CommandUserTask)

  implicit val userStateFormat = jsonFormat3(State)

  implicit val partClassFormat = jsonFormat3(PartyClassEntity)
}

