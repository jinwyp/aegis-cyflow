package com.yimei.cflow.organ.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._
import com.yimei.cflow.api.models.user.UserProtocol
import com.yimei.cflow.api.util.DBUtils
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.organ.db._
import DBUtils._
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.graph.cang.exception.BusinessException

import scala.concurrent.Future

/**
  * Created by hary on 16/12/19.
  */
class PartyRoute extends PartyClassTable with UserProtocol with SprayJsonSupport {

  import driver.api._

  //GET  /party?page=10&pageSize=20         参与方类别列表
  def getParty: Route = get {
    (pathPrefix("party") & parameter("page".as[Int]) & parameter("pageSize".as[Int])) { (page, pageSize) =>
      if(page <= 0 || pageSize <= 0) throw BusinessException("分页参数有误！")
      complete(dbrun(partClass.drop((page - 1) * pageSize).take(pageSize).result))
    }
  }

  //POST /party/:className/:description        创建参与方类别
  def createParty: Route = post {
    pathPrefix("party" / Segment / Segment) { (pc, pd) =>
      val entity: Future[PartyClassEntity] = dbrun(
        (partClass returning partClass.map(_.id)) into ((party, id) => party.copy(id = id)) += PartyClassEntity(None, pc, pd)
      )
      complete(entity)
    }
  }

  //GET  /party/:className                     查询参与方类别
  def queryParty: Route = get {
    pathPrefix("party" / Segment) { pc =>
      complete(dbrun(partClass.filter(p => p.class_name === pc).result))
    }
  }

  //PUT  /party/:id/:className/:description    更新参与方类别

  def updateParty: Route = put {
    pathPrefix("party" / Segment / Segment / Segment) { (id, pc, pd) =>
      val update = partClass.filter(_.id === id.toLong).map(p => (p.class_name, p.description)).update(pc, pd)
      val result = dbrun(update) map { count =>
        if (count > 0) "success" else "fail"
      }
      complete(result)
    }
  }

  def route: Route = getParty ~ createParty ~ queryParty ~ updateParty
}

object PartyRoute {
  def apply() = new PartyRoute

  def route: Route = PartyRoute().route
}
