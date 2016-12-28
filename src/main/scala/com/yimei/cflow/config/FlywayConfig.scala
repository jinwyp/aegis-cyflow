package com.yimei.cflow.config

import org.flywaydb.core.Flyway

/**
  * Created by hary on 16/12/2.
  */

trait FlywayConfig extends CoreConfig {

  private val flyway = new Flyway()
  flyway.setDataSource(
    coreConfig.getString("database.url"),
    coreConfig.getString("database.user"),
    coreConfig.getString("database.password")
  )

  // 设置migration表为指定表
  val schema = coreConfig.getString("flyway.schema")
  flyway.setTable(if(schema == null) "schema" else schema)


  def migrate = {
    flyway.migrate()
    this
  }

  def drop = {
    flyway.clean()
    this
  }
}





