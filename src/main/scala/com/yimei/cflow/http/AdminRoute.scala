package com.yimei.cflow.http

import java.sql.Timestamp
import java.time.Instant

import akka.actor.ActorRef
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.api.http.models.AdminModel._
import com.yimei.cflow.api.models.flow.{State => FlowState}
import com.yimei.cflow.api.services.ServiceProxy
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.core.FlowRegistry
import com.yimei.cflow.exception.{DatabaseException, ParameterException}
import com.yimei.cflow.api.models.database.FlowDBModel._
import com.yimei.cflow.api.models.database.UserOrganizationDBModel._
import com.yimei.cflow.core.db.FlowInstanceTable
import com.yimei.cflow.user.db._
import com.yimei.cflow.util.DBUtils.dbrun
import spray.json._

import scala.concurrent.Future


/**
  * Created by wangqi on 16/12/20.
  */
class AdminRoute(proxy: ActorRef) extends CoreConfig
  with PartyUserTable
  with PartyInstanceTable
  with FlowInstanceTable
  with AdminProtocol
  with SprayJsonSupport {

  import driver.api._

  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)


  /**
    * 创建流程
    *
    * @return
    */
  def createFlow = post {
    pathPrefix("flow") {
      pathPrefix("user" / Segment / Segment / Segment) { (party, instance_id, user_id) => {
        parameter("flowType") { flowType =>
          entity(as[Map[String, String]]) { init =>
            val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
              p.party_class === party &&
                p.instance_id === instance_id
            ).result.head) recover {
              case _ => throw new DatabaseException("不存在该公司")
            }

            def getUser(p: PartyInstanceEntity): Future[PartyUserEntity] = {
              dbrun(partyUser.filter(u =>
                u.user_id === user_id &&
                  u.party_id === p.id
              ).result.head) recover {
                case _ => throw new DatabaseException("不存在该用户")
              }
            }

            def insertFlow(p: PartyInstanceEntity, u: PartyUserEntity, s: FlowState): Future[FlowInstanceEntity] = {
              dbrun(flowInstance returning flowInstance.map(_.id) into ((ft, id) => ft.copy(id = id)) +=
                FlowInstanceEntity(None, s.flowId, flowType, p.party_class + "-" + p.instance_id, u.user_id,
                  s.toJson.toString, FlowRegistry.flowGraph(s.flowType).flowInitial ,0, Timestamp.from(Instant.now))) recover {
                case _ => throw new DatabaseException("添加流程错误")
              }
            }

            val f = for {
              p <- pi
              u <- getUser(p)
              s <- ServiceProxy.flowCreate(proxy, p.party_class + "-" + p.instance_id, u.user_id, flowType, init)
              f <- insertFlow(p, u, s)
            } yield f
            complete(f)
          }
        }
      }
      }
    }
  }

  /**
    * 根据flowId查询流程
    *
    * @return
    */
  def getFlowById = get {
    pathPrefix("flow" / Segment) { flowId =>
      val flow: Future[FlowInstanceEntity] = dbrun(flowInstance.filter(_.flow_id === flowId).result.head) recover {
        case _ => throw new DatabaseException("该流程不存在")
      }
      complete(for {
        f <- flow
        r <- ServiceProxy.flowGraph(proxy, f.flow_id)
      } yield {
        r
      })
    }
  }


  /**
    * hijack流程
    *
    * @return
    */
  def hijack = put {
    pathPrefix("flow/admin/hijack" / Segment) { flowId =>
      entity(as[HijackEntity]) { hjEntity =>
        val flow: Future[FlowInstanceEntity] = dbrun(flowInstance.filter(_.flow_id === flowId).result.head) recover {
          case _ => throw new DatabaseException("该流程不存在")
        }
        complete(for {
          f <- flow
          r <- ServiceProxy.flowHijack(proxy, f.flow_id, hjEntity.updatePoints, hjEntity.decision, hjEntity.trigger)
        } yield {
          r
        })
      }
    }
  }

  /**
    * 查询flows
    *
    * @return
    */
  def getFLows = get {
    pathPrefix("flow") {
      pathEnd {
        parameters(("flowId".?, "flowType".?, "userType".?, "userId".?, "status".as[Int].?, "page".as[Int].?, "pageSize".as[Int].?)).as(FlowQuery) { fq =>
          log.info("{}", fq)
          val q = flowInstance.filter { fi =>
            List(
              fq.flowId.map(fi.flow_id === _),
              fq.flowType.map(fi.flow_type === _),
              fq.userType.map(fi.user_type === _),
              fq.userId.map(fi.user_id === _),
              fq.status.map(fi.finished === _)
            ).collect({ case Some(a) => a }).reduceLeftOption(_ && _).getOrElse(true: Rep[Boolean])
          }

          val flows: Future[Seq[FlowInstanceEntity]] = (fq.page, fq.pageSize) match {
            case (Some(p), Some(ps)) if(p > 0 && ps > 0) => dbrun(q.drop((p - 1) * ps).take(ps).result)
            case _ => dbrun(q.result)
          }
          val total: Future[Int] = dbrun(q.length.result)

          val result: Future[FlowQueryResponse] = for {
            fs <- flows
            t <- total
          } yield {
            FlowQueryResponse(fs, t)
          }
          complete(result)
        }
      }
    }
  }


  def getFlowByUser = get {
    pathPrefix("flow/user" / Segment / Segment / Segment) { (party, instance_id, user_id) => {
      parameters(("flowType".?, "status".as[Int].?, "limit".as[Int].?, "offset".as[Int].?)).as(FlowQueryByUserEntity) { fq =>

        val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
          p.party_class === party &&
            p.instance_id === instance_id
        ).result.head) recover {
          case _ => throw new DatabaseException("不存在该公司")
        }

        def getUser(p: PartyInstanceEntity): Future[PartyUserEntity] = {
          dbrun(partyUser.filter(u =>
            u.user_id === user_id &&
              u.party_id === p.id
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该用户")
          }
        }


        def getQueryStatment(p: PartyInstanceEntity, u: PartyUserEntity) = {
          flowInstance.filter { fi =>
            fi.user_id === u.user_id &&
              fi.user_type === p.party_class + "-" + p.instance_id &&
              List(
                fq.flowType.map(fi.flow_type === _),
                fq.status.map(fi.finished === _)
              ).collect({ case Some(a) => a }).reduceLeftOption(_ && _).getOrElse(true: Rep[Boolean])
          }
        }


        def getAllFlows(p: PartyInstanceEntity, u: PartyUserEntity): Future[Seq[FlowInstanceEntity]] = {

          val q = getQueryStatment(p, u)

          (fq.limit, fq.offset) match {
            case (Some(l), Some(o)) => dbrun(q.drop(o).take(l).result)
            case _ => dbrun(q.result)
          }
        }

        def getFlowsCount(p: PartyInstanceEntity, u: PartyUserEntity): Future[Int] = {
          dbrun(getQueryStatment(p, u).length.result)
        }

        complete(for {
          p <- pi
          u <- getUser(p)
          alflow <- getAllFlows(p, u)
          total <- getFlowsCount(p, u)
        } yield {
          FlowQueryResponse(alflow, total)
        })

      }
    }
    }
  }


  /**
    * 得到任务中的数据节点
    * @return
    */
  def getGraph = get {
    pathPrefix("graph"/ Segment / Segment) { (graphType,taskName )=>
      complete(FlowRegistry.registries(graphType).userTasks(taskName))
    }
  }


  def route: Route = createFlow ~ getFlowById ~ hijack ~ getFLows ~ getFlowByUser ~ getGraph
}


object AdminRoute {

  //implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new AdminRoute(proxy)

  def route(proxy: ActorRef): Route = AdminRoute(proxy).route

}