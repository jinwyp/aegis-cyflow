package com.yimei.cflow.engine.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.api.models.database.FlowDBModel._
import com.yimei.cflow.api.util.DBUtils.dbrun
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.engine.db.DesignTable

import scala.concurrent.Future
/**
  * Created by hary on 16/12/28.
  */

class EditorRoute(proxy: ActorRef) extends CoreConfig with DesignTable {

  import driver.api._

  // 1> 用户列出所有流程设计  :   GET /design/graph
  def listDesign: Route =  get {
    pathPrefix("design/graph") {
      pathEnd {
        val result: Future[Seq[DesignEntity]] = dbrun(designClass.result)
        println(result)
        complete("ok")
      }
    }
  }

  // 2> 用户加载流程设计  :  GET /design/graph/:id  --> JSON
  def loadDesign: Route = get {
    path("design/graph" / Segment ) { id => {
//      val result = dbrun(designClass.filter(d => d.id.get === id).result.head)
      complete("ok")
    }
    }
  }

  // 3> 保存流程设计:      POST /design/graph/:id  + JSON
  def saveDesign: Route =  post {
    path("design/graph" / Segment) { id => {
//      entity(as[DesignEntity]) { designEntity =>
//        designEntity.id = id
//        designClass.insertOrUpdate(designEntity)
//        complete(s"id is $id")
//      }
      complete(id)
    }
    }
  }

  // 4> 下载模板项目:      GET /design/download/:id
  def download: Route = get {
    path("design/download" / Segment ) { id =>
      complete(s"download $id")
    }
  }

  // 总路由
  def route = listDesign ~ loadDesign ~ saveDesign ~ download
}

object EditorRoute {
  def apply(proxy: ActorRef) = new EditorRoute(proxy)
  def route(proxy: ActorRef): Route = EditorRoute(proxy).route
}

