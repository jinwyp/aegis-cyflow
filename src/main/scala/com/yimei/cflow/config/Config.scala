package com.yimei.cflow.config

import akka.actor.ActorSystem
import com.typesafe.config.Config
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import org.flywaydb.core.Flyway

/**
  * Created by hary on 16/12/2.
  */

trait FlywayConfig extends Core{

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

trait DatabaseConfig extends Core {

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

trait ApplicationConfig extends DatabaseConfig with FlywayConfig with Core

