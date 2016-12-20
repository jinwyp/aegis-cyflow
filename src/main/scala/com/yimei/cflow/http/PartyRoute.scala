package com.yimei.cflow.http

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.user.UserProtocol
import com.yimei.cflow.util.DBUtils._
import com.yimei.cflow.user.db._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import scala.concurrent.Future

/**
  * Created by hary on 16/12/19.
  */
class PartyRoute extends PartyClassTable with UserProtocol with SprayJsonSupport{

  import driver.api._

  def getParty:Route  = get {
    (pathPrefix("party") & parameter("limit".as[Int]) & parameter("offset".as[Int])) { (limit,offset) =>
        complete(dbrun(partClass.drop(offset).take(limit).result))
      }
    }

  def createParty: Route = post {
      pathEndOrSingleSlash {
        (parameter("class") & parameter("description")) { (pc, pd) =>
          val entity: Future[PartyClassEntity] = dbrun(
            (partClass returning partClass.map(_.id)) into ((party, id) => party.copy(id = id)) += PartyClassEntity(None, pc, pd)
          )
          complete(entity map { e => e})
        }
    }
  }

  def queryParty: Route = get {
    pathPrefix("party" / Segment) { pc =>
      complete(dbrun(partClass.filter(p => p.class_name === pc).result))
    }
  }

  def updateParty: Route = put {
    pathPrefix("party") {
      (parameter("id".as[Long]) & parameter("class") & parameter("description")) { (id, pc, pd) =>
        val update = partClass.filter(_.id === id).map(p => (p.class_name, p.description)).update(pc, pd)
        val result = dbrun(update) map { count =>
          if(count > 0) "success" else "fail"
        }
        complete(result)
      }
    }
  }

  def route: Route = getParty ~ createParty ~ queryParty ~ updateParty
}

object PartyRoute {

  //implicit val userServiceTimeout = Timeout(2 seconds)


  def apply() = new PartyRoute

  def route: Route = PartyRoute.route
}
