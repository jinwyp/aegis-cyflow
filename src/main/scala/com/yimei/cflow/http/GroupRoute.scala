package com.yimei.cflow.http

import java.sql.Timestamp
import java.time.Instant
import javax.ws.rs.Path

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.yimei.cflow.api.models.user.UserProtocol
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.DatabaseConfig.{coreExecutor => _, _}
import com.yimei.cflow.util.DBUtils._
import io.swagger.annotations.{ApiImplicitParams, ApiOperation, ApiResponses, _}

import scala.concurrent.Future
import scala.concurrent.duration._
import com.yimei.cflow.user.db._

class GroupRoute extends UserProtocol with PartyGroupTable with SprayJsonSupport {

  import driver.api._

  //GET    /group/:party_class?limit=10&offset=20     参与方运营组列表
  def getGroupParty: Route = get {
    pathPrefix("group" / Segment) { pc =>
      pathEnd {
        (parameter("limit".as[Int]) & parameter("offset".as[Int])) { (limit, offset) =>
          complete(dbrun(partyGroup.filter(_.party_class === pc).drop(offset).take(limit).result))
        }
      }
    }
  }

  //POST   /group/:party_class/:gid/description       创建参与方运营组
  def createGroupParty: Route = post {
    pathPrefix("group" / Segment / Segment / Segment) { (pc, gid, desc) =>
      val entity: Future[PartyGroupEntity] = dbrun(
        (partyGroup returning partyGroup.map(_.id)) into ((pg, id) => pg.copy(id = id)) += PartyGroupEntity(None, pc, gid, desc, Timestamp.from(Instant.now))
      )
      complete(entity map { e => e})
    }
  }

  //DELETE /group/:party_class/:gid                    删除参与方运营组
  def deleteGroupParty: Route = delete {
    pathPrefix("group" / Segment / Segment) { (pc, gid) =>
      val delete = partyGroup.filter(pg => pg.party_class === pc && pg.gid === gid).delete
      val result = dbrun(delete) map { count =>
        if (count > 0) "success" else "fail"
      }
      complete(result)
    }
  }

  //PUT    /group/id/:party_class/:gid/:description                更新参与方运营组
  def updateGroupParty: Route = put {
    pathPrefix("group" / Segment / Segment / Segment / Segment) { (id, pc, gid, desc) =>
        val update = partyGroup.filter(_.id === id.toLong).map(p => (p.party_class, p.gid, p.description)).update(pc, gid, desc)
        val result = dbrun(update) map { count =>
          if(count > 0) "success" else "fail"
        }
        complete(result)
      }
  }

  def route: Route = getGroupParty ~ createGroupParty ~ deleteGroupParty ~ updateGroupParty
}


/**
  * Created by hary on 16/12/2.
  */
object GroupRoute {

  implicit val userServiceTimeout = Timeout(2 seconds)


  def apply() = new GroupRoute()

  def route: Route = GroupRoute().route
}

