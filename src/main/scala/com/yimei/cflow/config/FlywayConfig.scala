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
  flyway.setTable("zflow_migration")

  def migrate = {
    flyway.migrate()
    this
  }

  def drop = {
    flyway.clean()
    this
  }
}





