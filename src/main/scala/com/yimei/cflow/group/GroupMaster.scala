package com.yimei.cflow.group

import akka.actor.Props
import com.yimei.cflow.integration.ModuleMaster
import com.yimei.cflow.config.GlobalConfig._

object GroupMaster {
  def props(userType: String, dependOn: Array[String]): Props = Props(new GroupMaster(userType, dependOn))
}

/**
  * Created by hary on 16/12/12.
  */
class GroupMaster(userType: String, dependOn: Array[String], persist: Boolean = true)
  extends ModuleMaster(module_group, dependOn)
    with GroupMasterBehavior{

  override def props(userType: String): Props = Group.props(userType, modules, persist)

}
