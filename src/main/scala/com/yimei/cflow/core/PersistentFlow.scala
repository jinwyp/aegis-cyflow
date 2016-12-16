package com.yimei.cflow.core

import java.util.{Date, UUID}

import akka.actor.{ActorRef, Props, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SaveSnapshotSuccess, SnapshotOffer}
import com.yimei.cflow.integration.DependentModule
import com.yimei.cflow.core.FlowRegistry._

import scala.concurrent.duration._

object PersistentFlow {
  def props(graph: FlowGraph,
            flowId: String,
            modules: Map[String, ActorRef],
            pid: String,
            guid: String): Props =
    Props(new PersistentFlow(graph, flowId, modules, pid, guid))
}

/**
  * Created by hary on 16/12/6.
  */

/**
  *
  * @param graph     流程图
  * @param flowId    流程id
  * @param dependOn  依赖的模块
  * @param pid       持久化id
  * @param guid      全局用户id
  */
class PersistentFlow(
          graph: FlowGraph,
          flowId: String,
          dependOn: Map[String, ActorRef],
          pid: String,
          guid: String) extends AbstractFlow with DependentModule with PersistentActor {

  import Flow._

  override def persistenceId: String = pid

  // 钝化超时时间
  val timeout = context.system.settings.config.getInt(s"flow.${graph.getFlowType}.timeout")
  log.info(s"timeout is $timeout")
  context.setReceiveTimeout(timeout seconds)

  override var state = State(flowId, guid, Map[String, DataPoint](), graph.getFlowInitial, Some(EdgeStart), Nil,graph.getFlowType)

  //
  override def genGraph(state: State): Graph = graph.getFlowGraph(state)

  override def modules: Map[String, ActorRef] = dependOn

  // 恢复
  def receiveRecover = {

    case ev: Event =>
      log.info(s"recover with event: $ev")
      updateState(ev)
    case SnapshotOffer(_, snapshot: State) =>
      state = snapshot
      log.info(s"snapshot recovered")
    case RecoveryCompleted =>
      logState("recovery completed")

  }

  override def receiveCommand: Receive = serving orElse commonBehavior

  // 命令处理
  val serving: Receive = {
    case cmd@CommandRunFlow(flowId) =>
      log.info(s"received ${cmd}")
      sender() ! genGraph(state)
      makeDecision

    case cmd: CommandPoint =>
      log.info(s"received ${cmd.name}")
      processCommandPoint(cmd)

    case cmds: CommandPoints =>
      log.info(s"received ${cmds.points.map(_._1)}")
      processCommandPoints(cmds)

    // 管理员更新数据点驱动流程
    case cmd: CommandUpdatePoints =>
      val uuid = UUID.randomUUID().toString
      val points: Map[String, DataPoint] = cmd.points.map { entry =>
        entry._1 -> DataPoint(entry._2, None, None, uuid, new Date().getTime)
      }
      persist(PointsUpdated(points)) { event =>
        log.info(s"${event} persisted")
        makeDecision()
        sender() ! genGraph(state) // 返回流程状态
      }

    // received 超时
    case ReceiveTimeout =>
      log.info(s"passivate timeout, begin passivating!!!!")
      context.stop(self)

    case SaveSnapshotSuccess(metadata) =>
      log.info(s"snapshot saved successfully")
  }

  override def unhandled(msg: Any): Unit = log.error(s"received unhandled message: $msg")

  // 处理命令
  protected def processCommandPoint(cmd: CommandPoint) = {

    persist(PointUpdated(cmd.name, cmd.point)) {
      event =>

        // 更新状体
        log.info(s"${event} persisted")
        updateState(event)

        // 作决定!!!
        makeDecision
    }
  }

  // 处理复合命令
  protected def processCommandPoints(cmds: CommandPoints) = {
    persist(PointsUpdated(cmds.points)) {
      event =>
        log.info(s"${event} persisted")
        updateState(event)
        makeDecision // 作决定
    }
  }

  protected def makeDecision(): Unit = {

    val cur = state.decision
    val arrow: Arrow = state.edge match {
      case None =>
        throw new IllegalArgumentException("impossible here")
      case Some(e) =>
        if (!e.check(state)) {
          Arrow(FlowTodo, None)
        } else {
          deciders(graph.getFlowType)(cur)(state)
        }
    }


    arrow match {
      case arrow@Arrow(j, Some(e)) =>
        logState("before judge")

        println(s"my ev is ${arrow}!!!!!!!!!!!!!!!!!!!!!!!!!!")
        persist(DecisionUpdated(arrow)) {
          event => log.info(s"${event} persisted")
            updateState(event)
            logState("after judge")
            log.info(s"check ${e}")

            // circular
            if (cur == j) {
              // clear the points from State where j.in is responsible for
              persist(null: Event) { event =>
                updateState(event) // @todo 王琦
              }
            } else {
              if (e.check(state)) {
                // 继续调度下一个节点,  maybe, 下一个节点不需要采集新的要素
                log.info(s"continue...")
                makeDecision()
              } else {
                log.info(s"schedule ${e}")
                e.schedule(state, modules)
              }
            }
        }
      case arrow@Arrow(FlowSuccess, None) =>
        logState("FlowSuccess")
        persist(DecisionUpdated(arrow)) {
          event =>
            log.info(s"${event} persisted, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case arrow@Arrow(FlowFail, None) =>
        logState("FlowFail")
        persist(DecisionUpdated(arrow)) {
          event =>
            log.info(s"${event} persisted, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case Arrow(FlowTodo, None) =>
        log.info(s"current decision [${state.decision}] result: FlowTodo")
    }
  }
}

