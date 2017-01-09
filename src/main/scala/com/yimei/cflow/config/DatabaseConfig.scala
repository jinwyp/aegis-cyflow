package com.yimei.cflow.config
import com.yimei.cflow.config.CoreConfig._
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}

/**
  * Created by hary on 16/12/6.
  */
object DatabaseConfig {

  private val hikariConfig = new HikariConfig()
  hikariConfig.setJdbcUrl(coreConfig.getString("database.url"))
  hikariConfig.setUsername(coreConfig.getString("database.user"))
  hikariConfig.setPassword(coreConfig.getString("database.password"))

  private val dataSource = new HikariDataSource(hikariConfig)
  val driver = slick.driver.MySQLDriver
  import driver.api.Database
  val db = Database.forDataSource(dataSource)
  db.createSession()

}
