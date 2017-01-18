package com.yimei.cflow.graph.cang.routes

import java.sql.Timestamp
import java.time.Instant

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
import com.yimei.cflow.graph.cang.config.Config
import com.yimei.cflow.graph.cang.session.{Session, SessionProtocol}

import scala.concurrent.Future

/**
  * Created by xl on 17/1/9.
  */

class DepositRoute extends DepositTable
  with DepositEntityProtocal
  with ResultProtocol
  with SprayJsonSupport
  with Session with SessionProtocol
  with Config {

  import driver.api._

  def createDeposit: Route = post {
    (path("deposits") & entity(as[AddDeposit])) { dp =>
      myRequiredSession { s =>
        val operator = s.userName

        val exist = dbrun(deposit.filter{dpt =>
          dpt.flowId === dp.flowId &&
            dpt.state =!= TRANSFERRED
        }.result)

        def addDeposit(exists: Seq[DepositEntity]): Future[DepositEntity] = {
          if(exists.length > 0) {
            throw BusinessException("已经存在未缴纳或者未确认到账的保证金记录")
          }
          dbrun(
            (deposit returning deposit.map(_.id) into ((dpst, id) => dpst.copy(id = id)) += DepositEntity(None, dp.flowId, dp.expectedAmount, 0, dp.state, dp.memo.getOrElse(""), operator, None, None))
          )
        }

        val result = for {
          e <- exist
          re <- addDeposit(e)
        } yield  Result(data = Some(re))

        complete(result)
      }
    }
  }

  def getDeposit: Route = get {
    path("deposits" / Segment) { flowId =>
      (parameter('page.as[Int].?) & parameter('count.as[Int].?) & parameter('state.as[String].?)) { (p, ps, st) =>
        myRequiredSession { s =>
          val page = if (p.isDefined) p.get else 1
          val pageSize = if (ps.isDefined) ps.get else 10
          val state = if (st.isDefined) st.get else NOTIFIED

          val dplist = dbrun(
            deposit.filter(dp => dp.flowId === flowId && dp.state === state).drop((page - 1) * pageSize).take(pageSize).result
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
  }

  def modifyDeposit: Route = put {
    path("deposits" / Segment) { flowId =>
      (parameter('state) & parameter('amount.as[Double].?)) { (state, amount) =>
        myRequiredSession { s =>
          val operator = s.userName
          val update = if(amount.isDefined) {
            if(state == TRANSFERRED){
              deposit.filter{ d => d.flowId === flowId && d.state === ALREADYPAID}.map(dp => (dp.state, dp.actuallyAmount , dp.operator, dp.ts_u)).update((state, amount.get, operator, Some(Timestamp.from(Instant.now))))
            } else {
              deposit.filter{ d => d.flowId === flowId && d.state === NOTIFIED}.map(dp => (dp.state, dp.actuallyAmount , dp.operator, dp.ts_u)).update((state, amount.get, operator, Some(Timestamp.from(Instant.now))))
            }
          } else {
            if(state == TRANSFERRED){
              deposit.filter{ d => d.flowId === flowId && d.state === ALREADYPAID}.map(dp => (dp.state, dp.operator, dp.ts_u)).update((state, operator, Some(Timestamp.from(Instant.now))))
            } else {
              deposit.filter{ d => d.flowId === flowId && d.state === NOTIFIED}.map(dp => (dp.state, dp.operator, dp.ts_u)).update((state, operator, Some(Timestamp.from(Instant.now))))
            }
          }

          val result = dbrun(update) map { count =>
            if (count > 0) "success" else "fail"
          } map { re => Result(data = Some(re)) }

          complete(result)
        }
      }
    }
  }

  def route = createDeposit ~ getDeposit ~ modifyDeposit
}

object DepositRoute {
  def apply() = new DepositRoute
  def route(): Route = DepositRoute().route
}
