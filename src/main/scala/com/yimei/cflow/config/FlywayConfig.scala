package com.yimei.cflow.config

import org.flywaydb.core.Flyway

/**
  * Created by hary on 16/12/2.
  */

trait FlywayConfig extends CoreConfig {

  private val flyway = new Flyway()
  flyway.setDataSource(
    config.getString("database.url"),
    config.getString("database.user"),
    config.getString("database.password")
  )

  def migrate = {
    flyway.migrate()
    this
  }

  def drop = {
    flyway.clean()
    this
  }
}





