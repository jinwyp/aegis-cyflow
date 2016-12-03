package com.yimei

import akka.actor.ActorRef

/**
  * Created by hary on 16/12/3.
  */
package object cflow {


  // 模块处在启动过程中, 不能服务
  /**
    * 采集点定义
    */
  val data_A = "A"
  val data_B = "B"
  val data_C = "C"
  val data_D = "D"
  val data_E = "E"
  val data_F = "F"
  val data_DEF = "DEF"



  // 采集那个data, 将来给到那个flow

  /**
    * 全局模块名称
    */
  val module_user = "UserMaster"
  val module_cang = "CangMaster"
  val module_ying = "YingMaster"
  val module_data = "DataMaster"

}
