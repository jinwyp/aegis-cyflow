package com.yimei.cflow.graph.cang.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import com.yimei.cflow.graph.cang.session.Session

/**
  * Created by xl on 16/12/28.
  */

//混入Session特质
class SessionDemoRoute extends SprayJsonSupport with Session{

  def loginRoute: Route = post {
    pathPrefix("login") {
      entity(as[MySession]) { body =>

        //设置session
        mySetSession(body) {
          complete("ok, just log in")
        }
      }
    }
  }

  def secretRoute: Route = get {
    pathPrefix("secret"){

      //获取session, 并取得session里面的值，session暂时只存放了userName和userId两个字段，后期根据需要添加
      myRequiredSession { session =>
        println(s"your name: ${session.userName} and your id: ${session.userId}")
        complete("ok, session is ok")
      }
    }
  }

  def logoutRoute: Route = get {
    pathPrefix("logout") {

      //使session失效
      myInvalidateSession {
        complete("logout, session is invalidate")
      }
    }
  }

  def route = loginRoute ~ secretRoute ~ logoutRoute
}

object SessionDemoRoute {
  def apply() = new SessionDemoRoute
  def route(): Route = SessionDemoRoute().route
}