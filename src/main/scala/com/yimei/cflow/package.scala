package com.yimei

import akka.actor.ActorRef

/**
  * Created by hary on 16/12/3.
  */
package object cflow {

  /**
    * 全局模块名称
    */
  val module_user = "UserMaster"
  val module_cang = "CangMaster"
  val module_ying = "YingMaster"
  val module_data = "DataMaster"

  /**
    * 所有的数据点名称
    */
  val  point_A = "A"
  val  point_B = "B"
  val  point_C = "C"
  val  point_D = "D"
  val  point_E = "E"
  val  point_F = "F"
  val  point_G = "G"
  val  point_H = "H"
  val  point_K = "K"
  val  point_U_A1 = "UA1"   // 用户采集
  val  point_U_A2 = "UA2"   // 用户采集

  /**
    * 数据采集点Actor名称
    */
  val data_A = "A"
  val data_B = "B"
  val data_C = "C"
  val data_DEF = "DEF"
  val data_GHK = "GHK"

  // 采集点actor, 与能够采集的数据点名称
  val dataPointMap = Map[String, Array[String]](
    data_A -> Array(point_A),
    data_B -> Array(point_B),
    data_C -> Array(point_C),
    data_DEF -> Array(point_D, point_E, point_F),
    data_GHK -> Array(point_G, point_H, point_K)
  )

  // 数据点描述
  val pointDescription = Map[String, String](
    point_A -> "征信平分1",
    point_B -> "征信平分2",
    point_C -> "征信平分3",
    point_D -> "征信平分4",
    point_E -> "征信平分5",
    point_F -> "征信平分6",
    point_G -> "征信平分7",
    point_H -> "征信平分9",
    point_K -> "征信平分9",
    point_U_A1 -> "用户提交1",
    point_U_A2 -> "用户提交2"
  )

  // 用户任务列表
  val task_A = "A"

  // 用户任务与采集点的对应关系
  val taskPointMap = Map[String, Array[String]](
    task_A  -> Array(point_U_A1, point_U_A2)
  )

}
