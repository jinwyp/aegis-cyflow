package com.yimei.cflow.core.db

import java.sql.Timestamp

import com.yimei.cflow.api.models.database.FlowDBModel._
import com.yimei.cflow.config.DatabaseConfig.driver

/**
  * Created by hary on 16/12/16.
  */

trait FlowTaskTable {
  import driver.api._

  class FlowTask(tag:Tag) extends Table[FlowTaskEntity](tag,"flow_task"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def flow_id = column[String]("flow_id")
    def task_id = column[String]("task_id")
    def task_name = column[String]("task_name")
    def task_submit = column[String]("task_submit")
    def user_type = column[String]("user_type")
    def user_id = column[String]("user_id")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id,flow_id,task_id,task_name,task_submit,user_type,user_id,ts_c) <> (FlowTaskEntity.tupled,FlowTaskEntity.unapply)
  }

  protected val flowTask = TableQuery[FlowTask]
}
