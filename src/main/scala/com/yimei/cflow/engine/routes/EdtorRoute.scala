package com.yimei.cflow.engine.routes

import akka.actor.ActorRef
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.api.models.database.FlowDBModel._
import com.yimei.cflow.api.util.DBUtils.dbrun
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.engine.db.DesignTable
import com.yimei.cflow.engine.routes.EditorObject.DesignDetail
import com.yimei.cflow.config.CoreConfig._
/**
  * Created by hary on 16/12/28.
  */

class EditorRoute(proxy: ActorRef) extends DesignTable with SprayJsonSupport {

  import com.yimei.cflow.engine.routes.EditorObject.DesignList
  import driver.api._
  // 1> 用户列出所有流程设计  :   GET /design/graph
  def listDesign: Route =  get {
    pathPrefix("design" / "graph") {
      pathEnd {
        val designList = dbrun(designClass.sortBy(d => d.ts_c).map(d => (d.id, d.name, d.ts_c)).result)
        val res = for (d <- designList) yield { d.map(d => DesignList(d._1.get, d._2, d._3.get))}
        complete(res)
      }
    }
  }

  // 2> 用户加载流程设计  :  GET /design/graph/:id  --> JSON
  def loadDesign: Route = get {
    path("design" / "graph" / LongNumber ) { id =>
      val design = dbrun(designClass.filter(d => d.id === id).result.head)
      complete(design.map(d => DesignDetail(d.id.get, d.name, d.json, d.meta, d.ts_c.get)))
    }
  }

  import com.yimei.cflow.engine.routes.EditorObject.SaveDesign
  // 3> 保存流程设计:      POST /design/graph?id=:id  + JSON
  def saveDesign: Route =  post {
    path("design" / "graph" ) {
      parameter("id".as[Long].?) { id =>
        entity(as[SaveDesign]) { design =>
          val designEntity = DesignEntity(id, design.name, design.json, design.meta, None)
          designClass.insertOrUpdate(designEntity)
          complete(StatusCodes.OK)
        }
      }
    }
  }


  // 4> 下载模板项目:      GET /design/download/:id
  def download: Route = get {
    path("design" / "download" / Segment ) { id =>
      complete(s"download $id")
    }
  }

  // 总路由
  def route = listDesign ~ loadDesign ~ saveDesign ~ download

}

object EditorRoute  {
  def apply(proxy: ActorRef) = new EditorRoute(proxy)
  def route(proxy: ActorRef): Route = EditorRoute(proxy).route
}

