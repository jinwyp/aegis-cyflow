package com.yimei.cflow.user

import akka.actor.{Actor, ActorLogging, Props, Terminated}
import com.yimei.cflow._
import com.yimei.cflow.config.Core
import com.yimei.cflow.integration.{ModuleMaster, ServicableBehavior}
import com.yimei.cflow.user.User.{HierarchyInfo, StartUser}


object UserMaster extends Core {
  def props() = Props(new UserMaster)
}

trait UserMasterBehavior extends Actor with ServicableBehavior with ActorLogging {
  override def serving: Receive = {
    case StartUser(userId) =>
      val hierarchyInfo = HierarchyInfo(None, None)
      context.child(userId).fold(create(userId, hierarchyInfo))(identity)
    // todo 王琦把这个改为从数据库读取用户的关系, 通过future来创建

    case command: User.Command =>
      val child = context.child(command.userId).fold {
        //todo 从数据库读取并创建  比如从neo4j
        val hierarchyInfo = HierarchyInfo(None, None)
        create(command.userId, hierarchyInfo)
      }(identity)

      // 转发消息
      child forward command

    case Terminated(child) =>
      log.info(s"${child.path.name} terminated")
  }

  private def create(userId: String, hierarchyInfo: HierarchyInfo) = {
    context.actorOf(Props(new User(userId, hierarchyInfo, 40)), userId)
  }
}

/**
  * Created by hary on 16/12/2.
  */
class UserMaster extends ModuleMaster(module_user, List(module_ying, module_cang)) with UserMasterBehavior


