package com.yimei.cflow.http

import akka.actor.ActorRef
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.config.CoreConfig

import scala.concurrent.Future

/**
  * Created by hary on 16/12/28.
  */
class DeployRoute (proxy: ActorRef) extends CoreConfig {

  def depoy = extractRequestContext { ctx =>
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

  def route: Route = ???
}


object DeployRoute {
  def apply(proxy: ActorRef) = new DeployRoute(proxy)
  def route(proxy: ActorRef): Route = DeployRoute(proxy).route
}

