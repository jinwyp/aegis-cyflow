package com.yimei.cflow

import java.io.{File, FileOutputStream}
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.graph.cang.models.CangFlowModel.{PayRequest, PayResponse}
import com.yimei.cflow.graph.cang.models.CangFlowModel._

import scala.concurrent.Future

class XieJieTestRoute extends ApplicationConfig with SprayJsonSupport {

  val f = Future {
    "abc"
  }

  def hello: Route = get {
    path("hello") {
      complete("Hello")
    }
  }

  def hello1: Route = get {
    pathPrefix("hello" / Segment) { id =>
      pathEnd(
        complete("Hello" + " -------  |||| " + id)
      )
    }
  }

  def hello2: Route = get {
    pathPrefix("hello" / Segment / Segment) { (id, name) =>
      pathEnd {
        println(" --- " + id + " --- " + name)
        complete("Hello" + " - " + id + " : " + name)
      }
    }
  }

  def hello3: Route = get {
    pathPrefix("hello" / Segment / Segment / Segment) { (id, name, phone) =>
      pathEnd {
        println(id + " --- " + name + " --- " + phone)
        complete("Test3" + " - " + id + " : " + name + " : " + phone)
      }
    }
  }

  import com.yimei.cflow.graph.cang.models.UserModel._

  def addUser: Route = post {
    pathPrefix("usertest") {
      pathEnd {
        entity(as[AddUser]) { user =>
          println(" -------- user --------- " + user.toString)
          complete(user)
        }
      }
    }
  }

  def test1 = post {
    path("pay"/"transfer"/"account") {
      entity(as[PayRequest]) { t =>
        complete(PayResponse(Some("11223344"),1,Some("我在处理")))
      }
    }
  }

  def test2 = get {
    path("pay"/"transaction"/"query") {
      parameter('transactionId){ t =>
        println(t)
        complete(PayQueryResponse(2,None))
      }
    }
  }


  def route: Route = hello ~ hello1 ~ hello2 ~ hello3 ~ addUser ~ test1 ~ test2


}

object XieJieTestRoute {

  def apply() = new XieJieTestRoute

  def route: Route = XieJieTestRoute().route
}


