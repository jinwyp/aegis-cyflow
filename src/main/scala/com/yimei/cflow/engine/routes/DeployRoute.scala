package com.yimei.cflow.engine.routes

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.mysql.cj.jdbc.Blob
import com.yimei.cflow.api.models.database.FlowDBModel.DeployEntity
import com.yimei.cflow.config.CoreConfig
import com.yimei.cflow.engine.routes.DeployObject.SaveDeploy

import scala.concurrent.Future

class DeployRoute (proxy: ActorRef) extends CoreConfig {


  // POST /deploy/:flowType  + fileupload
  def deploy = extractRequestContext { ctx =>
    implicit val materializer = ctx.materializer
    implicit val ec = ctx.executionContext

    // 实际作的应该是保存数据库, loadall
    fileUpload("csv") {
      case (metadata, byteSource) =>
        // sum the numbers as they arrive so that we can
        // accept any size of file
        //          byteSource.via(Framing.delimiter(ByteString("\n"), 1024))
        //            .mapConcat(_.utf8String.split(",").toVector)
        //            .map(_.toInt)
        //            .runFold(0) { (acc, n) => acc + n }
        val f: Future[Int] = null
        onComplete(f) { k =>
          complete("hello")
        }
    }
  }

  def saveDeploy = post {
    path("deploy") {
      parameter("id".as[Long].?) { id =>
        entity(as[SaveDeploy]) { deploy =>
          //val jar = deploy.jar.getBytes
          complete("ok")
        }
      }
    }
  }

  def route: Route = deploy ~ saveDeploy
}


object DeployRoute {
  def apply(proxy: ActorRef) = new DeployRoute(proxy)
  def route(proxy: ActorRef): Route = DeployRoute(proxy).route
}

