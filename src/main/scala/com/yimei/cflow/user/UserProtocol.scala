package com.yimei.cflow.user

import java.sql.Timestamp
import java.text.SimpleDateFormat

import com.yimei.cflow.core.FlowProtocol
import com.yimei.cflow.user.User.{CommandUserTask, State}
import com.yimei.cflow.user.db.{FlowTaskEntity, PartyClassEntity, PartyUserEntity}
import spray.json.{DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat}

/**
  * Created by hary on 16/12/6.
  */

trait UserProtocol extends DefaultJsonProtocol with FlowProtocol {

  implicit val userCommandUserTaskFormat = jsonFormat4(CommandUserTask)

  implicit val userStateFormat = jsonFormat3(State)

  implicit val partClassFormat = jsonFormat3(PartyClassEntity)

  implicit val partUserFormat = jsonFormat8(PartyUserEntity)

  implicit val flowTaskEntityFormat = jsonFormat8(FlowTaskEntity)

  implicit object TimeStampJsonFormat extends RootJsonFormat[Timestamp] {

    val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override def write(obj: Timestamp) = JsString(formatter.format(obj))

    override def read(json: JsValue) : Timestamp = json match {
      case JsString(s) => new Timestamp(formatter.parse(s).getTime)
      case _ => throw new DeserializationException("Error info you want here ...")
    }
  }
}

