package com.yimei.cflow.user

import akka.actor.{Actor, ActorLogging, Props, SupervisorStrategy, Terminated}
import com.yimei.cflow.user.User.{HierarchyInfo, StartUser}


object UserSupervisor {

}

/**
  * Created by hary on 16/12/2.
  */
class UserSupervisor  extends Actor with ActorLogging {
  override def supervisorStrategy: SupervisorStrategy = super.supervisorStrategy

  def receive = {
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

  def create(userId: String, hierarchyInfo: HierarchyInfo) = {
    context.actorOf(Props(new User(userId, hierarchyInfo, 40)), userId)
  }
}
