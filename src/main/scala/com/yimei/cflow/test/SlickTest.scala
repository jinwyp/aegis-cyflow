package com.yimei.cflow.test

import java.sql.Timestamp
import java.time.Instant

import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.config.FlywayConfig
import com.yimei.cflow.api.models.database.FlowDBModel._
import com.yimei.cflow.user.db.FlowInstanceTable
import com.yimei.cflow.util.DBUtils._

/**
  * Created by wangqi on 16/12/16.
  */
object SlickTest extends App with FlywayConfig with FlowInstanceTable{
  import driver.api._
  migrate

  dbrun(flowInstance returning flowInstance.map(_.id) += FlowInstanceEntity(None,"123","cang","rz-001","12345","somedata",0,Timestamp.from(Instant.now))) map {
    fid =>
      log.info("插入记录，Id："+fid)
      dbrun(flowInstance.filter(_.id===fid).result) map(
        f =>
          log.info("根据Id:{}查询结果：{}",fid,f)
      )
  }
}
