package com.yimei.cflow.asset.db

import com.yimei.cflow.api.models.database.UserOrganizationDBModel.PartyClassEntity
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/31.
  */
trait AssetTable {

  import driver.api._

  class PartyClass(tag:Tag) extends Table[PartyClassEntity](tag,"party_class"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def class_name = column[String]("class_name")
    def description = column[String]("description")
    def * = (id,class_name,description)<>(PartyClassEntity.tupled,PartyClassEntity.unapply)
  }

  protected val partClass = TableQuery[PartyClass]

}
