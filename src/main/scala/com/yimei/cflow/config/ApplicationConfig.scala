package com.yimei.cflow.config

import com.typesafe.config.ConfigFactory

/**
  * Created by hary on 16/12/6.
  */
trait ApplicationConfig extends FlywayConfig with CoreConfig {
  private val config = ConfigFactory.load()
  val url = config.getString("server.url")
}

