package com.yimei.cflow.config

import com.typesafe.config.ConfigFactory

/**
  * Created by hary on 16/12/6.
  */
trait ApplicationConfig extends FlywayConfig  {
  private val config = ConfigFactory.load()
  val url = config.getString("cangServer.url")
  val serverUrl = config.getString("server.url")
}

