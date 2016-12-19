package com.yimei.cflow.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.user.UserProtocol
import com.yimei.cflow.util.DBUtils._
import com.yimei.cflow.user.db._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

/**
  * Created by hary on 16/12/19.
  */
class PartyRoute extends PartyClassTable with UserProtocol with SprayJsonSupport{

  import driver.api._

  def getParty:Route  = get {
    (pathPrefix("party")&parameter("limit") & parameter("offset")) { (limit,offset) =>
        complete(dbrun(partClass.drop(offset.toInt).take(limit.toInt).result))
      }
    }
  def route: Route = getParty
}

object PartyRoute {

  //implicit val userServiceTimeout = Timeout(2 seconds)


  def apply() = new PartyRoute

  def route: Route = PartyRoute.route
}
