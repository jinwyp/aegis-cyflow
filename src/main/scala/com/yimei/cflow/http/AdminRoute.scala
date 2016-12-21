package com.yimei.cflow.http

import java.sql.Timestamp
import java.time.Instant

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.core.Flow
import com.yimei.cflow.core.Flow.DataPoint
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.UserProtocol
import com.yimei.cflow.user.db.{FlowInstanceEntity, _}
import com.yimei.cflow.util.DBUtils.dbrun
import spray.json.{DefaultJsonProtocol, _}

import scala.concurrent.Future

case class HijackEntity(updatePoints: Map[String, DataPoint], decision: Option[String], trigger: Boolean)

case class AllTasks(finishedTask:Seq[FlowInstanceEntity], processTask:Seq[FlowInstanceEntity],total:Int)

trait AdminProtocol extends DefaultJsonProtocol with UserProtocol {
  implicit val hijackEntityFormat = jsonFormat3(HijackEntity)
  implicit val allTaskFormat = jsonFormat3(AllTasks)
}

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

  /**
    * 创建流程
    * @return
    */
  def createFlow = post {
    pathPrefix("flow/user" / Segment / Segment / Segment) { (party,instance_id,user_id) =>{
      parameter("flowType") { flowType =>
        entity(as[Map[String,String]]) { init =>
          val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
            p.party_class === party         &&
              p.instance_id === instance_id
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该公司")
          }

          def getUser(p:PartyInstanceEntity): Future[PartyUserEntity] = {
            dbrun(partyUser.filter(u=>
              u.user_id=== user_id          &&
                u.party_id===p.id
            ).result.head) recover {
              case _ => throw new DatabaseException("不存在该用户")
            }
          }

          def insertFlow(p:PartyInstanceEntity, u:PartyUserEntity, s:Flow.State): Future[FlowInstanceEntity] = {
            dbrun(flowInstance returning flowInstance.map(_.id) into ((ft,id)=>ft.copy(id=id)) +=
              FlowInstanceEntity(None,s.flowId,flowType,p.party_class + "-" + p.instance_id,u.user_id,s.toJson.toString,0,Timestamp.from(Instant.now))) recover {
              case _ => throw new DatabaseException("添加流程错误")
            }
          }

          complete(for {
            p <- pi
            u <- getUser(p)
            s <- ServiceProxy.flowCreate(proxy,p.party_class + "-" + p.instance_id,u.user_id,flowType,init)
            f <- insertFlow(p,u,s)
          } yield {
            f
          })
        }
      }
    }
    }
  }

  /**
    * 根据flowId查询流程
    * @return
    */
  def getFlowById = get {
    pathPrefix("flow" / Segment) { flowId =>
      val flow: Future[FlowInstanceEntity] = dbrun(flowInstance.filter(_.flow_id===flowId).result.head) recover {
        case _ => throw new DatabaseException("该流程不存在")
      }
      complete(for {
        f <- flow
        r <- ServiceProxy.flowState(proxy,f.flow_id)
      } yield {
        r
      })
    }
  }


  /**
    *hijack流程
    * @return
    */
  def hijack = put {
    pathPrefix("flow/admin/hijack" / Segment) { flowId =>
      entity(as[HijackEntity]) { hjEntity =>
        val flow: Future[FlowInstanceEntity] = dbrun(flowInstance.filter(_.flow_id===flowId).result.head) recover {
          case _ => throw new DatabaseException("该流程不存在")
        }
        complete(for {
          f <- flow
          r <- ServiceProxy.flowHijack(proxy,f.flow_id,hjEntity.updatePoints,hjEntity.decision,hjEntity.trigger)
        } yield {
          r
        })
      }
    }
  }


  def getFlowByUser = get {
    pathPrefix("flow/user" / Segment / Segment / Segment) { (party,instance_id,user_id) => {
      (parameter('limit.as[Int])&parameter('offset.as[Int])&parameterMap) { (limit,offset,params) => {


        val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
          p.party_class === party         &&
            p.instance_id === instance_id
        ).result.head) recover {
          case _ => throw new DatabaseException("不存在该公司")
        }

        def getUser(p:PartyInstanceEntity): Future[PartyUserEntity] = {
          dbrun(partyUser.filter(u=>
            u.user_id=== user_id          &&
              u.party_id===p.id
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该用户")
          }
        }

        def getAllFlows(p:PartyInstanceEntity,u:PartyUserEntity): Future[Seq[FlowInstanceEntity]] = {
           params.contains("flowType") match {
            case true => dbrun(flowInstance.filter(f =>
              f.flow_type === params("flowType") &&
              f.user_id   === u.user_id &&
              f.user_type === p.party_class + "-" + p.instance_id
            ).drop(offset).take(limit).result)
            case false => dbrun(flowInstance.filter(f =>
                f.user_id   === u.user_id &&
                f.user_type === p.party_class + "-" + p.instance_id
            ).drop(offset).take(limit).result)
          }
        }

        def getFlowsCount(p:PartyInstanceEntity,u:PartyUserEntity): Future[Int] = {
          params.contains("flowType") match {
            case true => dbrun(flowInstance.filter(f =>
              f.flow_type === params("flowType") &&
                f.user_id   === u.user_id &&
                f.user_type === p.party_class + "-" + p.instance_id
            ).length.result)
            case false => dbrun(flowInstance.filter(f =>
              f.user_id   === u.user_id &&
                f.user_type === p.party_class + "-" + p.instance_id
            ).length.result)
          }
        }

        complete(for {
          p <- pi
          u <- getUser(p)
          alflow <- getAllFlows(p,u)
          total <- getFlowsCount(p,u)
        } yield {
          AllTasks(alflow.filter(_.finished==1),alflow.filter(_.finished==0),total)
        })
      }
      }
    }
    }
  }


  def route: Route = createFlow ~ getFlowById ~ hijack
}

object AdminRoute {

  //implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new AdminRoute(proxy)

  def route(proxy: ActorRef): Route = AdminRoute(proxy).route

}