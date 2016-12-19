package com.yimei.cflow.http

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.util.DBUtils._
/**
  * Created by hary on 16/12/19.
  */
class PartyRoute(proxy: ActorRef) {

  def getParty:Route  = get {
    (pathPrefix("party")&parameter("limit") & parameter("offset")) { (limit,offset) =>
        //dbrun()

        complete("success")
      }
    }


}

object PartyRoute {

  //implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new UserRoute(proxy)

  def route(proxy: ActorRef): Route = UserRoute(proxy).route
}
