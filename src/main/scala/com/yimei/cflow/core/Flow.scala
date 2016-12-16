package com.yimei.cflow.core

import akka.actor.ActorRef
import com.yimei.cflow.auto.AutoMaster._
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.core.FlowRegistry._
import com.yimei.cflow.group.GroupMaster.gfetch
import com.yimei.cflow.user.UserMaster.ufetch

object Flow {

  // 数据点: 值, 说明, 谁采集, 采集id, 采集时间
  case class DataPoint(value: String, memo: Option[String], operator: Option[String], id: String, timestamp: Long, used: Boolean = false)

  // create flow, but not run it
  case class CommandCreateFlow(flowType: String, guid: String)

  // response of CommandCreateFlow
  case class CreateFlowSuccess(flowId: String)

  case class RunFlowSuccess(flowId: String)

  // shutdown the flow

  // 接收命令
  trait Command {
    def flowId: String // flowType-userType-userId-uuid
  }

  // 启动流程
  case class CommandRunFlow(flowId: String) extends Command

  // 停止流程
  case class CommandShutdown(flowId: String) extends Command

  // 收到数据点
  case class CommandPoint(flowId: String, name: String, point: DataPoint) extends Command

  // 收到数据点集合  -- 同质数据点
  case class CommandPoints(flowId: String, points: Map[String, DataPoint]) extends Command

  // 查询流程
  case class CommandFlowGraph(flowId: String) extends Command

  // 查询流程状态
  case class CommandFlowState(flowId: String) extends Command

  // 手动更新points
  case class CommandUpdatePoints(flowId: String, points: Map[String, String]) extends Command

  // persistent事件
  trait Event

  case class PointUpdated(name: String, point: DataPoint) extends Event

  case class PointsUpdated(pionts: Map[String, DataPoint]) extends Event

  case class DecisionUpdated(arrow: Arrow) extends Event

  // 状态
  case class State(
                    flowId: String,
                    guid: String,
                    points: Map[String, DataPoint],
                    decision: String,
                    edge: Option[Edge],
                    histories: List[Arrow],
                    flowType:String
                  )


  trait EdgeBehavior {
    val autoTasks: Array[String]
    val userTasks: Array[String]
    val partUTasks: Map[String,Array[String]]   // key = 参与方guid对应的流程上下文key值, value = userTask名称列表
    val partGTasks: Map[String,Array[String]]   // key = 参与方ggid对应的流程上下文key值, value = userTask名称列表

    /**
      * 调度采集数据
      *
      * @param state   流程状态
      * @param modules 这个流程依赖的模块
      */
    def schedule(state: State, modules: Map[String, ActorRef]) = {

      //采集自动任务
      autoTasks.foreach(at =>
        fetch(state.flowType,at,state,modules(module_auto))
      )

      //采集用户任务
      userTasks.foreach(ut =>
        ufetch(state.flowType,ut,state,modules(module_user),state.guid)
      )

      // 参与方任务
      partUTasks.foreach { entry =>
        entry._2.foreach{ ut =>
          ufetch(state.flowType,ut,state,modules(module_user),state.points(entry._1).value)
        }
      }

      // 参与方组任务
      partGTasks.foreach{ entry =>
        entry._2.foreach{ gt =>
          gfetch(state.flowType,gt,state,modules(module_group),state.points(entry._1).value)
        }
      }
    }
    /**
      *
      * @param state
      * @return
      */
    def check(state: State): Boolean = {

      val pUserTasks: Array[String] = partUTasks.foldLeft(Array[String]())((t, put)=>t ++ put._2)
      val pGroupTasks: Array[String] = partGTasks.foldLeft(Array[String]())((t, gut)=> t ++ gut._2)

      val allUserTasks: Array[String] = userTasks ++ pUserTasks ++ pGroupTasks

      //对于指定的flowType和taskName 所需要的全部数据点， 如果当前status中的未使用过的数据点没有完全收集完，就返回false
      // autoTasks.foldLeft(true)((t,at) => t && !autoTask(state.flowType)(at)._1.exists(!state.points.filter(t=>(!t._2.used)).contains(_)) ) &&
      autoTasks.foldLeft(true)((t,at) => t && !autoTask(state.flowType)(at).points.exists(!state.points.filter(t=>(!t._2.used)).contains(_)) ) &&
      allUserTasks.foldLeft(true)((t,ut) => t && !userTask(state.flowType)(ut).exists(!state.points.filter(t=>(!t._2.used)).contains(_)))


       // partUTasks.foldLeft(true)((t,ptks) => t && ptks._2.foldLeft(true)((t1,au) => t1 && !userTask(state.flowType)(au).exists(!state.points.filter(t=>(!t._2.used)).contains(_))))



     }

    //获取全部不能重用的task
    def getNonReusedTask():(Array[String],Array[String]) = (autoTasks,userTasks)

    /**
      *根据（autoTask,userTask) 获取全部的数据点
 *
      * @return
      */
    def getAllDataPointsName(state: State):Array[String] = {
      val allTasks = getNonReusedTask()
      // allTasks._1.foldLeft(List[String]())((a,at) => autoTask(state.flowType)(at)._1 ++: a) ++
        allTasks._1.foldLeft(List[String]())((a,at) => autoTask(state.flowType)(at).points ++: a) ++
        allTasks._2.foldLeft(List[String]())((a,ut) => userTask(state.flowType)(ut) ++: a) toArray
    }



  }

  // 分支边
  case class Edge(
                 name: String,
                   autoTasks: Array[String] = Array(),
                   userTasks: Array[String] = Array(),
                   partUTasks: Map[String,Array[String]] = Map(),
                   partGTasks: Map[String,Array[String]] = Map()
                 ) extends EdgeBehavior {
    override def toString = name
  }

  // 开始边

  val EdgeStart = new Edge("Start") {

    override def schedule(state: State, modules: Map[String, ActorRef] = Map()) =
      throw new IllegalArgumentException("VoidEdge can not be scheduled")

    def check(state: State, autoTask: Array[String], userTask: Array[String]) = true

    override def toString = "Start"
  }

//  case object EdgeStart extends Edge(Array[String](),Array[String]()) {
//    override def schedule(state: State, modules: Map[String, ActorRef] = Map()) =
//      throw new IllegalArgumentException("VoidEdge can not be scheduled")
//
//    def check(state: State, autoTask: Array[String], userTask: Array[String]) = true
//
//    override def toString = "Start"
//  }



  val FlowSuccess = "FlowSuccess"
  val FlowFail = "FlowFail"
  val FlowTodo = "FlowTodo"

//  trait Decision {
//    def run(state: State): Arrow
//  }
//
//  trait Decided extends Decision
//
//  case object FlowSuccess extends Decided {
//    def run(state: State) = Arrow(FlowSuccess, None)
//
//    override def toString = "FlowSuccess"
//  }
//
//  case object FlowFail extends Decided {
//    def run(state: State) = Arrow(FlowFail, None)
//
//    override def toString = "FlowFail"
//  }
//
//  case object FlowTodo extends Decided {
//    def run(state: State) = Arrow(FlowTodo, None)
//
//    override def toString = "FlowTodo"
//  }

//  abstract class Judge extends Decision {
//
//    // 计算结果
//    def run(state: State): Arrow = {
//      state.edge match {
//        case None =>
//          throw new IllegalArgumentException("impossible here")
//        case Some(e) =>
//          if (!e.check(state)) {
//            Arrow(FlowTodo, None)
//          } else {
//            decide(state)
//          }
//      }
//    }
//
//    // 依据状态评估分支: success, 失败, 或者继续评估
//    def decide(state: State): Arrow
//  }

  case class Graph(edges: Map[Edge, Array[String]], state: State, dataDescription: Map[String, String])

  case class Arrow(end: String, edge: Option[Edge])



}




