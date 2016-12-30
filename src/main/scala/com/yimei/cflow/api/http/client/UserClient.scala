package com.yimei.cflow.api.http.client

import com.yimei.cflow.api.http.models.UserModel.{QueryUserResult, UserInfo, UserModelProtocol}
import com.yimei.cflow.api.models.database.UserOrganizationDBModel.UserGroupEntity
import com.yimei.cflow.api.models.user.State
import com.yimei.cflow.api.util.HttpUtil._
import spray.json._

import scala.concurrent.Future

/**
  * Created by hary on 16/12/23.
  */
trait UserClient extends UserModelProtocol{
  def createPartyUser(party: String, instance_id: String, userId: String, userInfo: String): Future[State] = {
    //访问com.yimei.cflow.http.UserRoute中的postUser接口
    sendRequest(
      path = "api/user",
      pathVariables = Array(party, instance_id, userId),
      method = "post",
      bodyEntity = Some(userInfo)) map { result =>
      result.parseJson.convertTo[State]
    }
  }

  def createUserGroup(party_id: String, gid: String, user_id: String): Future[UserGroupEntity] = {
    //访问com.yimei.cflow.http.GroupRoute中的createUserGroup接口
    sendRequest(
      path = "api/ugroup",
      pathVariables = Array(party_id, gid, user_id),
      method = "post"
    ) map { result =>
    result.parseJson.convertTo[UserGroupEntity]
    }
  }

  def updatePartyUser(party: String, instance_id: String, userId: String, userInfo: String): Future[String] = {
    //访问com.yimei.cflow.http.UserRoute中的putUser接口
    sendRequest(
      path = "api/user",
      pathVariables = Array(party, instance_id, userId),
      method = "put",
      bodyEntity = Some(userInfo)
    )
  }

  def getSpecificPartyUser(party: String, instance_id: String, userId: String): Future[QueryUserResult] = {
    //访问com.yimei.cflow.http.UserRoute中的getUser接口
    sendRequest(
      path = "api/user",
      pathVariables = Array(party, instance_id, userId),
      method = "get"
    ) map { result =>
      result.parseJson.convertTo[QueryUserResult]
    }
  }
}
