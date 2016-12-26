package com.yimei.cflow

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.scaladsl.Source
import akka.util.ByteString
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.graph.cang.models.UserModel
import com.yimei.cflow.graph.cang.models.UserModel.AddUser
import com.yimei.cflow.util.DBUtils._

import scala.concurrent.Future
import scala.util.Random

/**
  * Created by liuxinjie on 2016/12/23.
  */
class XieJieTestRoute extends App with ApplicationConfig with SprayJsonSupport {

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
        complete("Hello" + " -------|||| " + id)
      )
    }
  }

  val numbers = Source.fromIterator(() =>
    Iterator.continually(Random.nextInt()))

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

  import UserModel._

  def addUser: Route = post {
    pathPrefix("usertest" ) {
      pathEnd {
        entity(as[AddUser]) { user =>
          println(" -------- user --------- " + user.toString)
          complete(user)
        }
      }
    }
  }

  def route: Route = hello ~ hello1 ~ hello2 ~ hello3 ~ addUser
}

object XieJieTestRoute {

  def apply() = new XieJieTestRoute

  def route: Route = XieJieTestRoute().route
}
