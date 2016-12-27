package com.yimei.cflow.user.db

import java.sql.Timestamp

import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.api.http.models.CangDBModel.PartyGroupEntity

/**
  * Created by hary on 16/12/16.
  */

trait PartyGroupTable {
  import driver.api._

  class PartyGroup(tag:Tag) extends Table[PartyGroupEntity](tag,"party_group"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def party_class = column[String]("party_class")
    def gid = column[String]("gid")
    def description = column[String]("description")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id,party_class,gid,description,ts_c) <> (PartyGroupEntity.tupled,PartyGroupEntity.unapply)
  }

  protected val partyGroup = TableQuery[PartyGroup]

}
