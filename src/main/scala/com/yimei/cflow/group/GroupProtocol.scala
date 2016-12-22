package com.yimei.cflow.group

import com.yimei.cflow.api.models.group._
import spray.json.DefaultJsonProtocol

/**
  * Created by hary on 16/12/6.
  */

trait GroupProtocol extends DefaultJsonProtocol {

  implicit val groupCommandGroupTaskFormat = jsonFormat4(CommandGroupTask)

  implicit val groupStateFormat = jsonFormat3(State)
}

