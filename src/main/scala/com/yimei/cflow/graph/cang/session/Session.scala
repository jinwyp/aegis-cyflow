package com.yimei.cflow.graph.cang.session

/**
  * Created by xl on 16/12/26.
  */
import com.softwaremill.session.{SessioinConfig, SessionManager}
import com.yimei.cflow.config.CoreConfig

trait Session extends CoreConfig{
  val sessionConfig = SessionConfig.default(coreConfig.getString("http.session-key"))
  implicit val sessionManager = new SessionManager[Long](sessionConfig)
}
