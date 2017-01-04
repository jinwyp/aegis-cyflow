package com.yimei.cflow.graph.cang.session

/**
  * Created by xl on 16/12/26.
  */
import com.softwaremill.session._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import spray.json.DefaultJsonProtocol
import scala.util.Try

case class MySession(token: String, userName: String, userId: String, email: String, phone: String, party: String, instanceId: String, companyName: String)

trait Session {
  val sessionConfig = SessionConfig.fromConfig()
  implicit val sessionManager = new SessionManager[MySession](sessionConfig)
  implicit def serializer: SessionSerializer[MySession, String] = new MultiValueSessionSerializer(
    (ms: MySession) => Map(
      "token" -> ms.token,
      "name" -> ms.userName,
      "id" -> ms.userId,
      "email" -> ms.email,
      "phone" -> ms.phone,
      "party" -> ms.party,
      "instanceId" -> ms.instanceId,
      "companyName" -> ms.companyName),
    (msm: Map[String, String]) => Try {
      MySession(
        msm.get("token").get,
        msm.get("name").get,
        msm.get("id").get,
        msm.get("email").get,
        msm.get("phone").get,
        msm.get("party").get,
        msm.get("instanceId").get,
        msm.get("companyName").get)}
  )

  def mySetSession(mySession: MySession) = setSession(oneOff, usingCookies, mySession)
  val myRequiredSession = requiredSession(oneOff, usingCookies)
  val myInvalidateSession = invalidateSession(oneOff, usingCookies)
}

trait SessionProtocol extends DefaultJsonProtocol {
  implicit val mySessionFormat = jsonFormat8(MySession)
}
