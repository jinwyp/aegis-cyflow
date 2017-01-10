package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.util.DBUtils._
import com.yimei.cflow.graph.cang.db.{DepositEntityProtocal, DepositTable}
import com.yimei.cflow.graph.cang.models.DepositModel._
import com.yimei.cflow.graph.cang.db.Entities.DepositEntity
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.api.util.DBUtils
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.organ.db._
import DBUtils._
import com.yimei.cflow.api.http.models.ResultModel.{Error, PagerInfo, Result, ResultProtocol}
import com.yimei.cflow.config.CoreConfig._

import scala.concurrent.Future

/**
  * Created by xl on 17/1/9.
  */

class DepositRoute extends DepositTable with DepositEntityProtocal with ResultProtocol with SprayJsonSupport{
  val NOTIFIED = "notified"          // 保证金已通知
  val ALREADYPAID = "alreadyPaid"    // 保证金已缴纳
  val TRANSFERRED = "transferred"    // 保证金已到账

  import driver.api._

  def createDeposit: Route = post {
    (path("deposits") & entity(as[AddDeposit])) { dp =>
      val exist = dbrun(deposit.filter{dpt =>
        dpt.flowId === dp.flowId &&
        dpt.state === NOTIFIED
      }.result)

      def addDeposit(exists: Seq[DepositEntity]): Future[DepositEntity] = {
        if(exists.length > 0) {
          throw BusinessException("已经存在未缴纳的保证金记录")
        }
        dbrun(
          (deposit returning deposit.map(_.id) into ((dpst, id) => dpst.copy(id = id)) += DepositEntity(None, dp.flowId, dp.expectedAmount, 0, dp.state, dp.memo.getOrElse(""), None))
        )
      }

      val result = for {
        e <- exist
        re <- addDeposit(e)
      } yield  Result(data = Some(re))

      complete(result)
    }
  }

  def getDeposit: Route = get {
    path("deposits" / Segment) { flowId =>
      (parameter('page.as[Int].?) & parameter('count.as[Int].?)) { (p, ps) =>
        val page = if(p.isDefined) p.get else 1
        val pageSize = if(ps.isDefined) ps.get else 10

        val dplist = dbrun(
          deposit.filter(dp => dp.flowId === flowId).drop((page - 1) * pageSize).take(pageSize).result
        )

        val total = dbrun(
          deposit.length.result
        )

        val result = for {
          list <- dplist
          t <- total
        } yield Result(data = Some(list), success = true, meta = Some(PagerInfo(total = t, count = pageSize, offset = (page - 1) * pageSize, page = page)))

        complete(result)
      }
    }
  }

  def modifyDeposit: Route = put {
    path("deposits" / Segment / Segment) { (flowId, state) =>
      val update = deposit.filter(_.flowId === flowId).map(dp => (dp.state)).update(state)

      val result = dbrun(update) map { count =>
        if(count > 0) "success" else "fail"
      } map { re => Result(data = Some(re))}

      complete(result)
    }
  }

  def route = createDeposit ~ getDeposit ~ modifyDeposit
}

object DepositRoute {
  def apply() = new DepositRoute
  def route(): Route = DepositRoute().route
}
