package com.yimei.cflow.user.db

import java.sql.Timestamp
import com.yimei.cflow.config.DatabaseConfig.driver

/**
  * Created by xl on 16/12/19.
  */
case class UserGroupEntity(id: Option[Long], party_id: Long, gid: String, user_id: String, ts_c: Timestamp)

trait UserGroupTable {
  import driver.api._

  class UserGroup(tag:Tag) extends Table[UserGroupEntity](tag,"user_group") {
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def party_id = column[Long]("party_id")
    def gid = column[String]("gid")
    def user_id = column[String]("user_id")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id, party_id, gid, user_id, ts_c) <> (UserGroupEntity.tupled,UserGroupEntity.unapply)
  }

  protected val userGroup = TableQuery[UserGroup]
}
