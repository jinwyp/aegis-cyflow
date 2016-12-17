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
                    flowType: String
                  )


  case class PartUTask(guid: String, tasks: List[String])
  case class PartGTask(ggid: String, tasks: List[String])

  trait EdgeBehavior {
    val name: String
    val autoTasks: List[String]
    val userTasks: List[String]
    val partUTasks: List[PartUTask]
    val partGTasks: List[PartGTask] // key = 参与方ggid对应的流程上下文key值, value = userTask名称列表

    /*
      * 调度采集数据
      *
      * @param state   流程状态
      * @param modules 这个流程依赖的模块
      */
    def schedule(state: State, modules: Map[String, ActorRef]) = {

      if (name == "Start") {
        throw new IllegalArgumentException("StartEdge can not be scheduled")
      }

      //采集自动任务
      autoTasks.foreach(at =>
        fetch(state.flowType, at, state, modules(module_auto))
      )

      //采集用户任务
      userTasks.foreach(ut =>
        ufetch(state.flowType, ut, state, modules(module_user), state.guid)
      )

      // 参与方任务
      partUTasks.foreach { entry =>
        entry._2.foreach { ut =>
          ufetch(state.flowType, ut, state, modules(module_user), state.points(entry._1).value)
        }
      }

      // 参与方组任务
      partGTasks.foreach { entry =>
        entry._2.foreach { gt =>
          gfetch(state.flowType, gt, state, modules(module_group), state.points(entry._1).value)
        }
      }
    }

    /**
      *
      * @param state
      * @return
      */
    def check(state: State): Boolean = {

      // 没有任何任务!!!
      if (
        autoTasks.length == 0 &&
          userTasks.length == 0 &&
          partGTasks.size == 0 &&
          partGTasks == 0
      ) {
        true
      }

      val pUserTasks: Array[String] = partUTasks.foldLeft(Array[String]())((t, put) => t ++ put._2)
      val pGroupTasks: Array[String] = partGTasks.foldLeft(Array[String]())((t, gut) => t ++ gut._2)
      val allUserTasks: Array[String] = userTasks ++ pUserTasks ++ pGroupTasks

      //对于指定的flowType和taskName 所需要的全部数据点， 如果当前status中的未使用过的数据点没有完全收集完，就返回false
      autoTasks.foldLeft(true)((t, at) => t && !autoTask(state.flowType)(at).points.exists(!state.points.filter(t => (!t._2.used)).contains(_))) &&
        allUserTasks.foldLeft(true)((t, ut) => t && !userTask(state.flowType)(ut).exists(!state.points.filter(t => (!t._2.used)).contains(_)))
    }

    //获取全部不能重用的task
    def getNonReusedTask(): (Array[String], Array[String]) = (autoTasks, userTasks)

    /**
      * 根据（autoTask,userTask) 获取全部的数据点
      *
      * @return
      */
    def getAllDataPointsName(state: State): Array[String] = {
      val allTasks = getNonReusedTask()
      allTasks._1.foldLeft(List[String]())((a, at) => autoTask(state.flowType)(at).points ++: a) ++
        allTasks._2.foldLeft(List[String]())((a, ut) => userTask(state.flowType)(ut) ++: a) toArray
    }
  }

  //
  // 分支边
  // partUTasks:
  //   key为流程上下文的值, 这里的意思是: 对于这个map中的每个key, 将从上下文中取出这个key对应的值, 取出来的值是某个参与方的guid,
  //   value为, 这个参与方需要作的任务列表
  //
  // partGTasks:
  //   key为流程上下文的值, 这里的意思是: 对于这个map中的每个key, 将从上下文中取出这个key对应的值, 取出来的值是某个参与方的ggid(参与方运营组)
  //   value为, 这个参与方运营组需要作的任务列表
  //
  case class Edge(name: String,
                  autoTasks: Array[String] = Array(),
                  userTasks: Array[String] = Array(),
                  partUTasks: Map[String, Array[String]] = Map(),
                  partGTasks: Map[String, Array[String]] = Map() //
                 ) extends EdgeBehavior {
    override def toString = name
  }

  val EdgeStart = new Edge("Start") // start edge

  // common judges
  val FlowSuccess = "FlowSuccess"
  val FlowFail = "FlowFail"
  val FlowTodo = "FlowTodo"

  case class EdgeDescription(
                              autoTasks: Array[String] = Array(),
                              userTasks: Array[String] = Array(),
                              partUTasks: Map[String, Array[String]] = Map(),
                              partGTasks: Map[String, Array[String]] = Map(),
                              begin: String,
                              end: String
                            )

  case class Graph(edges: Map[String, EdgeDescription], state: State, dataDescription: Map[String, String])

  case class Arrow(end: String, edge: Option[Edge])

}




