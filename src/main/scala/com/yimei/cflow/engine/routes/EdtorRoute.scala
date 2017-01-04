package com.yimei.cflow.engine.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.engine.db.DesignTable

/**
  * Created by hary on 16/12/28.
  */

class EditorRoute(proxy: ActorRef) extends CoreConfig with DesignTable {

  // 1> 用户列出所有流程设计  :   GET /design/graph
  def listDesign: Route =  get {
    pathPrefix("design/graph") {
      pathEnd {
        complete("ok")
      }
    }
  }

  // 2> 用户加载流程设计  :  GET /design/graph/:id  --> JSON
  def loadDesign: Route = get {
    path("design/graph" / Segment ) { id =>
      complete(s"loadDesign $id")
    }
  }

  // 3> 保存流程设计:      POST /design/graph/:id  + JSON
  def saveDesign: Route =  post {
    path("design/graph" / Segment) { id =>
      complete(s"id is $id")
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

