package com.yimei.cflow.http

/**
  * Created by hary on 16/12/19.
  */
import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.util.DBUtils._
import com.yimei.cflow.user.db._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import scala.concurrent.Future
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._
import com.yimei.cflow.api.models.user.UserProtocol

class InstRoute extends PartyInstanceTable with UserProtocol with SprayJsonSupport{
  import driver.api._

  //POST /inst/:party_class/:instance_id/:party_name          创建参与方实例
  def createPartyInstance: Route = post {
    pathPrefix("inst" / Segment / Segment / Segment) { (pc, ii, pn) =>
      val entity: Future[PartyInstanceEntity] = dbrun(
        (partyInstance returning partyInstance.map(_.id)) into ((pi, id) => pi.copy(id = id)) += PartyInstanceEntity(None, pc, ii, pn, Timestamp.from(Instant.now))
      )
      complete(entity)
    }
  }

  //GET  /inst/:party/:instance_id           查询参与方实例
  def queryPartyInstance: Route = get {
    pathPrefix("inst" / Segment / Segment) { (pc, ii) =>
      complete(dbrun(partyInstance.filter(p => p.party_class === pc && p.instance_id === ii).result.head))
    }
  }

  //PUT  /inst/:id/:party/:instance_id/:party_name          更新参与方实例
  def updatePartyInstance: Route = put {
    pathPrefix("inst" / Segment / Segment / Segment / Segment) { (id, pc, ii, pn) =>
      val update = partyInstance.filter(_.id === id.toLong).map(p => (p.party_class, p.instance_id, p.party_name)).update(pc, ii, pn)
      val result = dbrun(update) map { count =>
        if(count > 0) "success" else "fail"
      }
      complete(result)
    }
  }

  def route: Route = createPartyInstance ~ queryPartyInstance ~ updatePartyInstance
}

object InstRoute {
  def apply() = new InstRoute
  def route: Route = InstRoute().route
}