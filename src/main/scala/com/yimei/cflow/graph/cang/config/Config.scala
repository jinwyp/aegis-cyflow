package com.yimei.cflow.graph.cang.config

import com.typesafe.config.ConfigFactory

/**
  * Created by wangqi on 16/12/26.
  */
object Config {
  private val config = ConfigFactory.load()

  val url = config.getString("server.url")


  val rzf = "rzf"
  val flowType = "cang"
  val zjfUserId = "zjf-88888888!77777"
  val zjfFinanceId = "zjf-88888888!88888"
}
