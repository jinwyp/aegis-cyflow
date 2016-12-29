package com.yimei.cflow.organ.db

import java.sql.Timestamp

import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._

/**
  * Created by hary on 16/12/16.
  */


trait PartyUserTable {
  import driver.api._

  class PartyUser(tag:Tag) extends Table[PartyUserEntity](tag,"party_user"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def party_id = column[Long]("party_id")
    def user_id = column[String]("user_id")
    def password = column[String]("password")
    def phone = column[Option[String]]("phone")
    def email = column[Option[String]]("email")
    def name = column[String]("name")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id,party_id,user_id,password,phone,email,name,ts_c) <> (PartyUserEntity.tupled,PartyUserEntity.unapply)
  }

  protected val partyUser = TableQuery[PartyUser]
}
