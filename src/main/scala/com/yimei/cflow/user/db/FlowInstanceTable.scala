package com.yimei.cflow.user.db
import com.yimei.cflow.config.DatabaseConfig._

import java.sql.Timestamp

/**
  * Created by hary on 16/12/16.
  */
case class FlowInstanceEntity(id:Option[Long], flow_id:String, flow_type:String, user_type:String, user_id:String, state:String, finished:Int, ts_c:Timestamp)

trait FlowInstanceTable {
  import driver.api._

  class FlowInstance(tag:Tag) extends Table[FlowInstanceEntity](tag,"flow_instance"){
    def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
    def flow_id = column[String]("flow_id")
    def flow_type = column[String]("flow_type")
    def user_type = column[String]("user_type")
    def user_id = column[String]("user_id")
    def state = column[String]("state")
    def finished = column[Int]("finished")
    def ts_c = column[Timestamp]("ts_c")

    def * = (id,flow_id,flow_type,user_type,user_id,state,finished,ts_c) <> (FlowInstanceEntity.tupled,FlowInstanceEntity.unapply)
  }

  protected val flowInstance = TableQuery[FlowInstance]
}
