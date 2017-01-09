package com.yimei.cflow.config

import org.flywaydb.core.Flyway
import com.yimei.cflow.config.CoreConfig._

/**
  * Created by hary on 16/12/2.
  */

trait FlywayConfig {

  private val flyway = new Flyway()
  flyway.setDataSource(
    coreConfig.getString("database.url"),
    coreConfig.getString("database.user"),
    coreConfig.getString("database.password")
  )

  // 设置migration表为指定表
  val schema = try {
    val a = coreConfig.getString("flyway.schema")
    if (a == null || a == "") "schema" else a
  } catch {
    case _ => "schema"
  }
  flyway.setTable(schema)


  def migrate = {
    flyway.migrate()
    this
  }

  def drop = {
    flyway.clean()
    this
  }
}





