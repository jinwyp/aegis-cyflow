package com.yimei.cflow.engine.db

import java.sql.{Blob, Timestamp}

import com.yimei.cflow.api.models.database.FlowDBModel.DeployEntity
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/28.
  */
trait DeployTable {

  import driver.api._

  class Deploy(tag: Tag) extends Table[DeployEntity](tag, "design") {
    def id         = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def flow_type  = column[String]("flow_type")
    def jar        = column[Blob]("jar")
    def enable     = column[Boolean]("enable")
    def ts_c       = column[Timestamp]("ts_c")

    def * = (id, flow_type, jar, enable, ts_c) <>(DeployEntity.tupled, DeployEntity.unapply)
  }

  protected val deply = TableQuery[Deploy]

}
