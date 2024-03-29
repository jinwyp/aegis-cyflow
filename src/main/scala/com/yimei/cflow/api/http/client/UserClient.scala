package com.yimei.cflow.api.http.client

import com.yimei.cflow.api.http.models.UserModel._
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.{PartyInstanceEntity, PartyUserEntity, UserGroupEntity}
import com.yimei.cflow.api.models.user.State
import com.yimei.cflow.api.util.HttpUtil._
import com.yimei.cflow.graph.cang.models.UserModel.{UserData, UserInfoList}
import com.yimei.cflow.graph.cang.session.{MySession, SessionProtocol}
import spray.json._
import com.yimei.cflow.config.CoreConfig._

import scala.concurrent.Future

/**
  * Created by hary on 16/12/23.
  */
trait UserClient extends UserModelProtocol with SessionProtocol {
  def createPartyUser(party: String, instance_id: String, userId: String, userInfo: String): Future[State] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的postUser接口
    sendRequest(
      path = "api/internal/user",
      pathVariables = Array(party, instance_id, userId),
      method = "post",
      bodyEntity = Some(userInfo)) map { result =>
      result.parseJson.convertTo[State]
    }
  }

  def createUserGroup(party_id: String, gid: String, user_id: String): Future[UserGroupEntity] = {
    //访问com.yimei.cflow.organ.routes.GroupRoute中的createUserGroup接口
    sendRequest(
      path = "api/internal/ugroup",
      pathVariables = Array(party_id, gid, user_id),
      method = "post"
    ) map { result =>
    result.parseJson.convertTo[UserGroupEntity]
    }
  }

  def updatePartyUser(party: String, instance_id: String, userId: String, userInfo: String): Future[String] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的putUser接口
    sendRequest(
      path = "api/internal/user",
      pathVariables = Array(party, instance_id, userId),
      method = "put",
      bodyEntity = Some(userInfo)
    )
  }

  def updatePartyUserEmailAndPhone(username: String, email: String, phone: String): Future[String] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的modifyEmailAndPhone接口
    sendRequest(
      path = "api/internal/user",
      pathVariables = Array(username, email, phone, "emailAndPhone"),
      method = "put"
    )
  }

  def getSpecificPartyUser(party: String, instance_id: String, userId: String): Future[QueryUserResult] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的getUser接口
    sendRequest(
      path = "api/internal/user",
      pathVariables = Array(party, instance_id, userId),
      method = "get"
    ) map { result =>
      result.parseJson.convertTo[QueryUserResult]
    }
  }

  def getLoginUserInfo(userInfo: String): Future[UserGroupInfo] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的getLoginUserInfo接口
    sendRequest(
      path = "api/internal/login",
      method = "post",
      bodyEntity = Some(userInfo)
    ) map { result =>
      result.parseJson.convertTo[UserGroupInfo]
    }
  }

  def disableUser(username: String): Future[String] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的disAbleUser接口
     sendRequest(
      path = "api/internal/disable",
      pathVariables = Array(username),
      method = "get"
    )
  }

  def getAllUserList(page: Int, pageSize: Int, dynamicQuery: String): Future[UserInfoList] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的getAllUserInfo接口
    sendRequest(
      path = "api/internal/alluser",
      paramters = Map("page" -> page.toString, "pageSize" -> pageSize.toString),
      method = "post",
      bodyEntity = Some(dynamicQuery)
    ) map { result =>
      result.parseJson.convertTo[UserInfoList]
    }
  }

  def getSpecificUserInfoByUsername(username: String): Future[UserGroupInfo] = {
    //访问com.yimei.cflow.organ.routes.UserRoute中的getUserInfoByUserName接口
    sendRequest(
      path = "api/internal/specificUser",
      pathVariables = Array(username),
      method = "get"
    ) map { result =>
      result.parseJson.convertTo[UserGroupInfo]
    }
  }
}
