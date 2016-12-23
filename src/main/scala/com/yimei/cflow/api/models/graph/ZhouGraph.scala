package com.yimei.cflow.api.models.graph

import com.yimei.cflow.api.annotation._
import com.yimei.cflow.api.models.flow.{Arrow, State}
import com.yimei.cflow.auto.AutoMaster.CommandAutoTask

import scala.concurrent.Future

/**
  * Created by hary on 16/12/23.
  */

@GraphProperty(
  initial = "V0",
  graphType = "zhou"
)
class ZhouGraph {

  @Points
  val points = Map()

  /////////////////////////////////////////////////////////////////////////////
  // 决策点设置
  /////////////////////////////////////////////////////////////////////////////
  @VertexProperty( description = "决定1")
  def V0(state: State): Seq[Arrow] = ???

  @VertexProperty( description = "决定1" )
  def V1(state: State): Seq[Arrow] = ???

  @VertexProperty( description = "决定1" )
  def V2(state: State): Seq[Arrow] = ???

  @VertexProperty( description = "决定1" )
  def V3(state: State): Seq[Arrow] = ???

  @VertexProperty( description = "决定1" )
  def V4(state: State): Seq[Arrow] = ???

  @VertexProperty( description = "决定1" )
  def V5(state: State): Seq[Arrow] = ???


  ///////////////////////////////////////////////////////////////////////////////////////
  // 自动任务定义
  ///////////////////////////////////////////////////////////////////////////////////////
  @AutoTask( description = "A", points = Array("A") )
  def autoA(autoTask: CommandAutoTask): Future[Map[String, String]] = ???

  @AutoTask( description = "B", points = Array("B") )
  def autoB(autoTask: CommandAutoTask): Future[Map[String, String]] = ???

  @AutoTask( description = "C", points = Array("C") )
  def autoC(autoTask: CommandAutoTask): Future[Map[String, String]] = ???

  @AutoTask( description = "DEF", points = Array("D", "E", "F") )
  def autoDEF(autoTask: CommandAutoTask): Future[Map[String, String]] = ???

  ///////////////////////////////////////////////////////////////////////////////////////
  // 用户任务定义
  ///////////////////////////////////////////////////////////////////////////////////////
  @UserTask( points = Array("UA1", "UA2") )
  val UA =  "用户提交表单A"

  ///////////////////////////////////////////////////////////////////////////////////////
  // 参与方任务设置
  ///////////////////////////////////////////////////////////////////////////////////////
  @PartyUserTask( guidKey = "融资方审批", tasks = Array("UA1", "UA2") )
  val puTask1 = "从guidKey中"

  ///////////////////////////////////////////////////////////////////////////////////////
  // 参与方组任务设置
  ///////////////////////////////////////////////////////////////////////////////////////
  @PartyGroupTask( ggidKey = "融资方审批", tasks = Array("UA1", "UA2") )
  val puTask2 = ???

  ///////////////////////////////////////////////////////////////////////////////////////
  // 边设置
  ///////////////////////////////////////////////////////////////////////////////////////
  @EdgeProperty( begin = "V0", end = "V1", autoTasks = Array("A", "B", "C") )
  val E1 = ???

  @EdgeProperty( begin = "V1", end = "V2", userTasks = Array("TKPU1", "TKPG1") )
  val E2 = ???

  @EdgeProperty( begin = "V2", end = "V3", pgTasks = Array("pg1") )
  val E3 = ???

  @EdgeProperty( begin = "V3", end = "V4", userTasks = Array("UA") )
  val E4 = ???
}

