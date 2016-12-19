package com.yimei.cflow.user.db

import java.sql.Timestamp

import com.yimei.cflow.config.DatabaseConfig.driver

/**
  * Created by hary on 16/12/16.
  */
case class PartyUserEntity(id:Option[Long], party_id:Long, user_id:String, password:String, phone:Option[String], email:Option[String], name:String, gid:Option[String], ts_c:Timestamp)

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
    def gid = column[Option[String]]("gid")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id,party_id,user_id,password,phone,email,name,gid,ts_c) <> (PartyUserEntity.tupled,PartyUserEntity.unapply)
  }

  protected val partyUser = TableQuery[PartyUser]
}
