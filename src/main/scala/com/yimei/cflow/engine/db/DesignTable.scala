package com.yimei.cflow.engine.db

import java.sql.Timestamp

import com.yimei.cflow.api.models.database.FlowDBModel.DesignEntity
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/28.
  */
trait DesignTable {

  import driver.api._

  class DesignClass(tag: Tag) extends Table[DesignEntity](tag, "design") {
    def id        = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name      = column[String]("name")
    def json      = column[Option[String]]("json")
    def meta      = column[Option[String]]("meta")
    def ts_c      = column[Option[Timestamp]]("ts_c")

    def * = (id, name, json, meta, ts_c) <> (DesignEntity.tupled, DesignEntity.unapply)
  }

  protected val designClass = TableQuery[DesignClass]

}
