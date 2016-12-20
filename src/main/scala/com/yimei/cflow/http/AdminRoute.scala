package com.yimei.cflow.http

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.core.Flow.DataPoint
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.UserProtocol
import com.yimei.cflow.user.db._
import com.yimei.cflow.util.DBUtils.dbrun
import spray.json.DefaultJsonProtocol

import scala.concurrent.Future

case class HijackEntity(updatePoints: Map[String, DataPoint], decision: Option[String], trigger: Boolean)

trait AdminProtocol extends DefaultJsonProtocol with UserProtocol {
  implicit val hijackEntityFormat = jsonFormat3(HijackEntity)
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

          complete(for {
            p <- pi
            u <- getUser(p)
            r <- ServiceProxy.flowCreate(proxy,p.party_class + "-" + p.instance_id,u.user_id,flowType,init)
          } yield {
            r
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
  def getFlow = get {
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


  def route: Route = createFlow ~ getFlow ~ hijack
}

object AdminRoute {

  //implicit val userServiceTimeout = Timeout(2 seconds)


  def apply(proxy: ActorRef) = new AdminRoute(proxy)

  def route(proxy: ActorRef): Route = AdminRoute(proxy).route

}