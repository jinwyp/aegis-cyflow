package com.yimei.cflow.user.db

import java.sql.Timestamp

import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.api.http.models.CangDBModel.PartyClassEntity

/**
  * Created by wangqi on 16/12/19.
  */



trait PartyClassTable {
  import driver.api._

  class PartyClass(tag:Tag) extends Table[PartyClassEntity](tag,"party_class"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def class_name = column[String]("class_name")
    def description = column[String]("description")
    def * = (id,class_name,description)<>(PartyClassEntity.tupled,PartyClassEntity.unapply)
  }

  protected val partClass = TableQuery[PartyClass]

}
