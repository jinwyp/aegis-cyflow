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
  val point_X = "px"
  val point_Y = "py"


  /**
    * 所有数据点的描述
    */
  val pointDescription = Map[String, String](
    point_A -> "xxxxx",
    point_B -> "xxxxx"
  )

  /**
    * 数据采集点Actor名称
    */
  val data_AB = "AB"

  /**
    * 数据采集点与数据点对应关系
    */
  val dataPointMap: Map[String, Array[String]] = Map(
    data_AB -> Array(point_A, point_B)
  )


  /**
    * 所有用户任务
    */
  val task_A = "A"


  /**
    * 所有用户任务与数据点的对应关系
    */
  val taskPointMap: Map[String, Array[String]] = Map(
    task_A -> Array(point_X,point_Y)
  )

}
