package com.yimei.cflow.graph.mult

/**
  * Created by wangqi on 16/12/13.
  */
object MultConfig {
  /**
    * 所有的数据点名称
    */
  val point_A = "A"
  val point_B = "B"
  val point_C = "C"
  val point_KPU_1 = "KPU1" //设置partition user1
  val point_KPG_1 = "KPG1" //设置partition group1

  val point_PU1 = "PU1" //partition user1 采集数据点1
  val point_PU2 = "PU2" //partition user1 采集数据点2

  val point_PG1 = "PG1" //partition group1 采集数据点1
  val point_PG2 = "PG2" //partition group1 采集数据点2

  val point_U_A1 = "UA1" //流程发起者任务数据
  val point_U_A2 = "UA2" //流程发起者任务数据

  val point_D = "D" //自动任务数据点
  val point_E = "E"
  val point_F = "F"

  /**
    * 数据采集点Actor名称
    */
  val auto_A = "A"
  val auto_B = "B"
  val auto = "C"
  val auto_DEF = "DEF"

  /**
    * 流程类型
    */
  val flow_ying = "ying"

  // 采集点actor, 与能够采集的数据点名称
  val autoPointMap = Map[String, Array[String]](
    auto_A -> Array(point_A),
    auto_B -> Array(point_B),
    auto -> Array(point_C),
    auto_DEF -> Array(point_D, point_E, point_F)
  )

  // 数据点描述
  val pointDescription = Map[String, String](
    point_A -> "征信平分1",
    point_B -> "征信平分2",
    point_C -> "征信平分3",
    point_D -> "征信平分4",
    point_E -> "征信平分5",
    point_F -> "征信平分6",
    point_U_A1 -> "用户提交A1",
    point_U_A2 -> "用户提交A2",
    point_KPU_1 -> "设置资金方 user1", //设置partition user1
    point_KPG_1 -> "设置融资方 group1", //设置partition group1
    point_PU1 -> "partition user1 采集数据点1", //partition user1 采集数据点1
    point_PU2 -> "partition user1 采集数据点2", //partition user1 采集数据点2
    point_PG1 -> "partition group1 采集数据点1", //partition group1 采集数据点1
    point_PG2 -> "partition group1 采集数据点2" //partition group1 采集数据点2
  )

  // 用户任务列表
  val task_A = "UA"
  //用户设置partition
  val task_K_PU1 = "TKPU1"
  val task_K_PG1 = "TKPG1"
  //partition任务列表
  val task_PU = "PU"
  val task_PG = "PG"


  // 用户任务与采集点的对应关系
  val taskPointMap = Map[String, Array[String]](
    task_A -> Array(point_U_A1, point_U_A2),
    task_K_PU1 -> Array(point_KPU_1),
    task_K_PG1 -> Array(point_KPG_1),
    task_PU -> Array(point_PU1, point_PU2),
    task_PG -> Array(point_PG1, point_PG2)
  )

  // 决策点
  val J0 = "V0"
  val J1 = "V1"
  val J2 = "V2"
  val J3 = "V3"
  val J4 = "V4"
  val J5 = "V5"

  // 决策点描述
  val judgeDecription = Map[String, String](
    J0 -> "V0",
    J1 -> "V1",
    J2 -> "V2",
    J3 -> "V3",
    J4 -> "V4",
    J5 -> "V5"
  )
}

///////////////////////////////////////////////////////////////////////////////////////
//      \ ----------------------> always true                     VoidEdge
//      V0
//       \ ---------------------> A(data_A) B(data_B) C(data_C)   E1
//       V1
//         \-------------------->  point_K_PU1,point_K_PG1        E2
//         v2
//           \------------------> [pu_1,pu_2](pg-1,pg-2)          E3
//            V3
//           /  \---------------> [UA1, UA2](task_A)              E4
//          /    V4
//         /      \-------------> [data_def](partTask_A)          E5
//         --<----V5
//             |
//             |---------------->                                 EdgeStart
///////////////////////////////////////////////////////////////////////////////////////