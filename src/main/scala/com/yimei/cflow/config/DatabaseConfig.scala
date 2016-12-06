package com.yimei.cflow.config

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

/**
  * Created by hary on 16/12/6.
  */
trait DatabaseConfig extends CoreConfig {

  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(config.getString("database.url"))
  hikariConfig.setUsername(config.getString("database.user"))
  hikariConfig.setPassword(config.getString("database.password"))

  private val dataSource = new HikariDataSource(hikariConfig)
  val driver = slick.driver.MySQLDriver;
  import driver.api.Database;
  val db = Database.forDataSource(dataSource)
  db.createSession()

}
