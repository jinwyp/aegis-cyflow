package com.yimei.cflow.group

import com.yimei.cflow.group.Group.CommandGroupTask
import com.yimei.cflow.group.Group.State
import spray.json.DefaultJsonProtocol

/**
  * Created by hary on 16/12/6.
  */

trait GroupProtocol extends DefaultJsonProtocol {

  implicit val userCommandGroupTaskFormat = jsonFormat4(CommandGroupTask)

  implicit val userStateFormat = jsonFormat3(State)
}
