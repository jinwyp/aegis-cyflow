package com.yimei.cflow.graph.cang.session

/**
  * Created by xl on 16/12/26.
  */
import com.softwaremill.session._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import spray.json.DefaultJsonProtocol
import scala.util.Try


trait Session extends DefaultJsonProtocol{
  val sessionConfig = SessionConfig.fromConfig()
  implicit val sessionManager = new SessionManager[MySession](sessionConfig)
  implicit def serializer: SessionSerializer[MySession, String] = new MultiValueSessionSerializer(
    (ms: MySession) => Map("name" -> ms.userName, "id" -> ms.userId),
    (msm: Map[String, String]) => Try { MySession(msm.get("name").get, msm.get("id").get)}
  )

  def mySetSession(mySession: MySession) = setSession(oneOff, usingCookies, mySession)
  val myRequiredSession = requiredSession(oneOff, usingCookies)
  val myInvalidateSession = invalidateSession(oneOff, usingCookies)

  //session
  case class MySession(userName: String, userId: String)

  implicit val mySessionFormat = jsonFormat2(MySession)
}
