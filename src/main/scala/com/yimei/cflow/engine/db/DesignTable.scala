package com.yimei.cflow.engine.db

import java.sql.Timestamp

import com.yimei.cflow.api.models.database.FlowDBModel.DesignEntity
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/28.
  */
trait DesignTable {

  import driver.api._

  class Design(tag: Tag) extends Table[DesignEntity](tag, "design") {
    def id        = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def name      = column[String]("name")
    def json      = column[String]("json")
    def meta      = column[String]("meta")
    def ts_c      = column[Timestamp]("ts_c")

    def * = (id, name, json, meta, ts_c) <> (DesignEntity.tupled, DesignEntity.unapply)
  }

  protected val design = TableQuery[Design]

}
