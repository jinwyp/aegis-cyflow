package com.yimei.cflow.graph.cang.session

/**
  * Created by xl on 16/12/26.
  */
import com.softwaremill.session._
import com.softwaremill.session.SessionDirectives._
import com.softwaremill.session.SessionOptions._
import spray.json.DefaultJsonProtocol
import scala.util.Try

case class MySession(userName: String, userId: String, party: String, gid: Option[String], instanceId: String, companyName: String)

trait Session {
  val sessionConfig = SessionConfig.fromConfig()
  implicit val sessionManager = new SessionManager[MySession](sessionConfig)
  implicit def serializer: SessionSerializer[MySession, String] = new MultiValueSessionSerializer(
    (ms: MySession) => Map(
      "name" -> ms.userName,
      "id" -> ms.userId,
      "party" -> ms.party,
      "gid" -> ms.gid.getOrElse(""),
      "instanceId" -> ms.instanceId,
      "companyName" -> ms.companyName),
    (msm: Map[String, String]) => Try {
      val gid = if(msm.get("gid").get == "") None else Some(msm.get("gid").get)
      MySession(
        msm.get("name").get,
        msm.get("id").get,
        msm.get("party").get,
        gid,
        msm.get("instanceId").get,
        msm.get("companyName").get)}
  )

  def mySetSession(mySession: MySession) = setSession(oneOff, usingCookies, mySession)
  val myRequiredSession = requiredSession(oneOff, usingCookies)
  val myInvalidateSession = invalidateSession(oneOff, usingCookies)
}

trait SessionProtocol extends DefaultJsonProtocol {
  implicit val mySessionFormat = jsonFormat6(MySession)
}
