package com.yimei.cflow.api.http.models

import com.yimei.cflow.api.models.flow.DataPoint
import com.yimei.cflow.api.models.group.{GroupProtocol, State}
import com.yimei.cflow.api.models.user.UserProtocol
import spray.json.DefaultJsonProtocol

/**
  * Created by xl on 16/12/23.
  */
object TaskModel {

  case class UserSubmitEntity(flowId:String,taskName:String,points:Map[String,DataPoint])

  case class GroupTaskResult(tasks:Seq[State],total:Int)

  case class UserSubmitMap(memo:Option[String],value: String)


  trait TaskProtocol extends DefaultJsonProtocol with UserProtocol with GroupProtocol{

    //implicit val userTaskEntityFormat = jsonFormat3(DataPoint)
    implicit val userSubmintEntity = jsonFormat3(UserSubmitEntity)
    implicit val groupTaskFromat = jsonFormat2(GroupTaskResult)
    implicit val userSubmitMapFormat = jsonFormat2(UserSubmitMap)

  }
}
