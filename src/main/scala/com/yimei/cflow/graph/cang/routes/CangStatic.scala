package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.config.FreemarkerConfig._

/**
  * Created by xl on 17/1/3.
  */
class CangStatic {

  def getStatic: Route = get {
    path("src-cang" / "frontend" ) {
      getFromDirectory(staticPathAdmin)
    }
  }

}

object CangStatic {
  def apply() = new CangStatic

  def route(): Route = CangStatic().getStatic
}
