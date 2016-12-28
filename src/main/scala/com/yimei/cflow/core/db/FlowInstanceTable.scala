package com.yimei.cflow.core.db

import java.sql.Timestamp

import com.yimei.cflow.api.models.database.FlowDBModel._
import com.yimei.cflow.config.DatabaseConfig._

/**
  * Created by hary on 16/12/16.
  */

trait FlowInstanceTable {
  import driver.api._

  class FlowInstance(tag:Tag) extends Table[FlowInstanceEntity](tag,"flow_instance"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def flow_id = column[String]("flow_id")
    def flow_type = column[String]("flow_type")
    def user_type = column[String]("user_type")
    def user_id = column[String]("user_id")
    def data = column[String]("data")
    def state = column[String]("state")
    def finished = column[Int]("finished")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id,flow_id,flow_type,user_type,user_id,data,state,finished,ts_c) <> (FlowInstanceEntity.tupled,FlowInstanceEntity.unapply)
  }

  protected val flowInstance = TableQuery[FlowInstance]
}
