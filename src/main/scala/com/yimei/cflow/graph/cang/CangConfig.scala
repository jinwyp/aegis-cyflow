package com.yimei.cflow.graph.cang

/**
  * Created by hary on 16/12/15.
  */
object CangConfig {

  /**
    * 所有的数据点名称
    */
  val point_A = "pa"
  val point_B = "pb"


  /**
    *  所有数据点的描述
    */
  val pointDescription = Map[String, String]()

  /**
    * 数据采集点Actor名称
    */
  val data_X = "X"
  val data_Y = "Y"

  /**
    *  数据采集点与数据点对应关系
    */
  val dataPointMap: Map[String, Array[String]] = _


  /**
    *  所有用户任务
    */
  val task_A = "A"


  /**
    * 所有用户任务与数据点的对应关系
    */

  val taskPointMap: Map[String, Array[String]]  = _
}
