package com.yimei.cflow.http

import java.sql.{SQLIntegrityConstraintViolationException, Timestamp}
import java.time.Instant

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig.driver
import com.yimei.cflow.core.Flow.DataPoint
import com.yimei.cflow.core.{Flow, FlowProtocol}
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.group.{Group, GroupProtocol}
import com.yimei.cflow.integration.ServiceProxy
import com.yimei.cflow.user.db._
import com.yimei.cflow.user.{User, UserProtocol}
import com.yimei.cflow.util.DBUtils.dbrun
import spray.json.{DefaultJsonProtocol, _}

import scala.concurrent.Future

/**
  * Created by hary on 16/12/6.
  */
//用户提交任务数据
//case class UserPoint(value:String,memo:Option[String],operator:Option[String])
case class UserSubmitEntity(flowId:String,taskName:String,points:Map[String,DataPoint])

case class GroupTaskResult(tasks:Seq[Group.State],total:Int)


trait TaskProtocol extends DefaultJsonProtocol with UserProtocol with GroupProtocol{

  //implicit val userTaskEntityFormat = jsonFormat3(DataPoint)
  implicit val userSubmintEntity = jsonFormat3(UserSubmitEntity)
  implicit val groupTaskFromat = jsonFormat2(GroupTaskResult)

}

class TaskRoute(proxy: ActorRef) extends UserProtocol
  with FlowProtocol
  with SprayJsonSupport
  with PartyInstanceTable
  with PartyUserTable
  with FlowTaskTable
  with UserGroupTable
  with TaskProtocol
  with CoreConfig {
  import driver.api._

  /**
    *用户当前任务
    * @return
    */
  def getUTask = get {
    pathPrefix("utask" / Segment / Segment / Segment) { (party,instance_id,user_id) =>
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

      val tasks: Future[User.State] = for {
        p <- pi
        u <- getUser(p)
        r <- ServiceProxy.userQuery(proxy,p.party_class+"-"+p.instance_id,u.user_id)
      } yield {
        r
      }
      complete(tasks)
    }
  }

  /**
    *用户历史任务
    * @return
    */
  def getUTaskHistory = get{
    pathPrefix("utask" / Segment / Segment / Segment) { (party,instance_id,user_id) =>
      parameter("history") { histor =>
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

        def getTasks(p:PartyInstanceEntity, u:PartyUserEntity): Future[Seq[FlowTaskEntity]] = {
          dbrun(flowTask.filter(f=>
            f.user_type === p.party_class+"-"+p.instance_id &&
            f.user_id   === u.user_id
          ).result)
        }

        val tasks: Future[Seq[FlowTaskEntity]] = for {
          p <- pi
          u <- getUser(p)
          r <- getTasks(p,u)
        } yield {
          r
        }
        complete(tasks)
      }
    }
  }


  /**
    * 用户提交任务
    * @return
    */
  def putTask = put {
    pathPrefix("utask" / Segment / Segment / Segment / Segment) { (party,instance_id,user_id,task_id) =>
      entity(as[UserSubmitEntity]) { entity=>
        //查询用户所在公司信息
        val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
          p.party_class === party         &&
            p.instance_id === instance_id
        ).result.head) recover {
          case _ => throw new DatabaseException("不存在该公司")
        }
        //查询用户信息
        def getUser(p:PartyInstanceEntity): Future[PartyUserEntity] = {
          dbrun(partyUser.filter(u=>
            u.user_id=== user_id          &&
              u.party_id===p.id
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该用户")
          }
        }
        //插入数据库
        def insertTask(p:PartyInstanceEntity, u:PartyUserEntity): Future[FlowTaskEntity] = {
          dbrun(flowTask returning flowTask.map(_.id) into ((fl,id)=>fl.copy(id=id)) +=
            FlowTaskEntity(None,entity.flowId,task_id,entity.taskName,entity.points.toJson.toString,p.party_class+"-"+p.instance_id,u.user_id,Timestamp.from(Instant.now))
          ) recover {
            case a:SQLIntegrityConstraintViolationException => throw new DatabaseException("当前任务已被提交")
          }
        }

        //提交任务，并返回当前用户的任务
        val result: Future[User.State] = for {
          p <- pi
          u <- getUser(p)
          f <- insertTask(p,u)
          s <- ServiceProxy.userSubmit(proxy,f.user_type,f.user_id,f.task_id,entity.points)
        } yield {
          s
        }
      complete(result)
      }

    }
  }


  def getGTask = get {
    pathPrefix("gtask" / Segment / Segment / Segment ) { (party, instance_id, user_id) =>
      (parameter('limit.as[Int])&parameter('offset.as[Int])) { (limit,offset) =>
        //查询用户所在公司信息
        val pi: Future[PartyInstanceEntity] = dbrun(partyInstance.filter(p =>
          p.party_class === party         &&
            p.instance_id === instance_id
        ).result.head) recover {
          case _ => throw new DatabaseException("不存在该公司")
        }
        //查询用户信息
        def getUser(p:PartyInstanceEntity): Future[PartyUserEntity] = {
          dbrun(partyUser.filter(u=>
            u.user_id=== user_id          &&
              u.party_id===p.id
          ).result.head) recover {
            case _ => throw new DatabaseException("不存在该用户")
          }
        }

        //获取全部的组
        def getGroups(p:PartyInstanceEntity,u:PartyUserEntity): Future[Seq[UserGroupEntity]] = {
          dbrun(userGroup.filter(ug=>
            ug.user_id === u.user_id &&
            ug.party_id === p.id.get
          ).drop(offset).take(limit).result)
        }


        def getGropCount(p:PartyInstanceEntity,u:PartyUserEntity): Future[Int] = {
          dbrun(userGroup.filter(ug=>
            ug.user_id === u.user_id &&
              ug.party_id === p.id.get
          ).length.result)
        }

        def getTasks(ugs:Seq[UserGroupEntity],p:PartyInstanceEntity): Future[Seq[Group.State]] = {
         Future.sequence(ugs.map(ug => ServiceProxy.groupQuery(proxy,p.party_class+"-"+p.instance_id,ug.gid)))
        }

        val r: Future[GroupTaskResult] = for {
          p <- pi
          u <- getUser(p)
          num <- getGropCount(p,u)
          gs: Seq[UserGroupEntity] <- getGroups(p,u)
          tsk: Seq[Group.State] <- getTasks(gs,p)
        } yield {
          GroupTaskResult(tsk,num)
        }

        complete(r)
      }


    }
  }


  /**
    * 用户claim任务
    * @return
    */
  def claimTask = put {
    pathPrefix("gtask" / Segment / Segment / Segment / Segment / Segment) { (party, instance_id, user_id, task_id, gid) => {
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

      def getGroup(p: PartyInstanceEntity, u: PartyUserEntity) = {
        dbrun(userGroup.filter(ug =>
          ug.party_id === p.id &&
            ug.user_id === u.user_id &&
            ug.gid === gid
        ).result.head) recover {
          case _ => throw new DatabaseException("该用户不属于该群组")
        }
      }

      complete(for {
        p <- pi
        u <- getUser(p)
        g <- getGroup(p, u)
        r <- ServiceProxy.groupClaim(proxy, p.party_class + "-" + p.instance_id, g.gid, u.user_id, task_id)
      } yield {
        r
      })
    }
    }
  }

  /**
    * 发起自动任务
    * @return
    */
  def autoTask = post {
    pathPrefix("auto"/ Segment / Segment / Segment) { (flowType,flowId,taskName) =>


      val state: Future[Flow.State] = ServiceProxy.flowState(proxy,flowId)

      complete(for {
        s <- state
      } yield {
        ServiceProxy.autoTask(proxy,s,flowType,taskName)
        "success"
      })
    }
  }




  def route: Route = getUTask ~ getUTaskHistory ~ putTask ~ getGTask ~ claimTask ~ autoTask
}

/**
  * Created by hary on 16/12/2.
  */
object TaskRoute {
  def apply(proxy: ActorRef) = new TaskRoute(proxy)
  def route(proxy: ActorRef): Route = TaskRoute(proxy).route
}

