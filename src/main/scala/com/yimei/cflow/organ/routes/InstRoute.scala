package com.yimei.cflow.organ.routes

/**
  * Created by hary on 16/12/19.
  */
import java.sql.Timestamp
import java.time.Instant

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.http.models.PartyModel.{PartyInstanceInfo, _}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._
import com.yimei.cflow.api.models.user.UserProtocol
import com.yimei.cflow.api.util.DBUtils
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.organ.db._
import DBUtils._
import com.yimei.cflow.graph.cang.exception.BusinessException

import scala.concurrent.Future

class InstRoute extends PartyInstanceTable with UserProtocol with PartyModelProtocal with SprayJsonSupport{
  import driver.api._

  //POST /inst         创建参与方实例
  //body {party: String, instanceId: String, companyName: String}
  def createPartyInstance: Route = post {
    pathPrefix("inst") {
      entity(as[PartyInstanceInfo]) { info =>

        val getPartyInstance: Future[Seq[PartyInstanceEntity]] = dbrun(
          partyInstance.filter( p =>
            p.instance_id === info.instanceId &&
            p.party_class === info.party
          ).result
        )

        def entity(pies: Seq[PartyInstanceEntity]): Future[PartyInstanceEntity] = {
          if(pies.length == 0) {
            dbrun(
              (partyInstance returning partyInstance.map(_.id)) into ((pi, id) => pi.copy(id = id)) += PartyInstanceEntity(None, info.party, info.instanceId, info.companyName, Timestamp.from(Instant.now))
            )
          } else {
            throw BusinessException("已存在该公司")
          }

        }

       val result = for {
          pi <- getPartyInstance
          r <- entity(pi)
        } yield r

        complete(result)
      }
    }
  }

  //GET  /inst/:party/:instance_id           查询参与方实例
  def queryPartyInstance: Route = get {
    pathPrefix("inst" / Segment / Segment) { (pc, ii) =>
      complete(dbrun(partyInstance.filter(p => p.party_class === pc && p.instance_id === ii).result))
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

  //GET /inst/list?page=x&pageSize=y
  def getPartyInstanceList: Route = get {
    (path("inst" / "list") & parameter('page.as[Int]) & parameter('pageSize.as[Int])) { (page, pageSize) =>
      if(page <= 0 || pageSize <= 0)
        throw BusinessException("分页参数错误")

      def getPartyInstanceList: Future[Seq[PartyInstanceEntity]] = dbrun(partyInstance.drop((page - 1) * pageSize).take(pageSize).result)
      def getAccount = dbrun(partyInstance.length.result)

      val result = for {
        list <- getPartyInstanceList
        account <- getAccount
      } yield PartyInstanceListEntity(list, account)

      complete(result)
    }
  }


  def route: Route = createPartyInstance ~ queryPartyInstance ~ updatePartyInstance ~ getPartyInstanceList
}

object InstRoute {
  def apply() = new InstRoute
  def route: Route = InstRoute().route
}