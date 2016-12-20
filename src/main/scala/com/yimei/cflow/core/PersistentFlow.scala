package com.yimei.cflow.core

import java.util.{Date, UUID}

import akka.actor.{ActorRef, Props, ReceiveTimeout}
import akka.persistence.{PersistentActor, RecoveryCompleted, SaveSnapshotSuccess, SnapshotOffer}
import com.yimei.cflow.core.FlowRegistry._

import scala.concurrent.duration._

object PersistentFlow {

  def props(graph: FlowGraph,
            flowId: String,
            modules: Map[String, ActorRef],
            pid: String,
            guid: String,
            initData: Map[String, String]
           ): Props =
    Props(new PersistentFlow(graph, flowId, modules, pid, guid, initData))
}

/**
  * Created by hary on 16/12/6.
  */

/**
  *
  * @param graph  流程图
  * @param flowId 流程id
  * @param pid    持久化id
  * @param guid   全局用户id
  */
class PersistentFlow(
                      graph: FlowGraph,
                      flowId: String,
                      modules: Map[String, ActorRef],
                      pid: String,
                      guid: String,
                      initData: Map[String, String]
                    ) extends AbstractFlow with PersistentActor {

  import Flow._

  override def persistenceId: String = pid

  // 钝化超时时间
  val timeout = graph.getTimeout

  log.info(s"timeout is $timeout")
  context.setReceiveTimeout(timeout seconds)

  val initPoints = initData.map{ entry =>
    (entry._1, DataPoint(entry._2, None, None, "init", new Date().getTime, false))
  }

  override var state = State(flowId, guid, initPoints, graph.getFlowInitial, Some(EdgeStart), Nil, graph.getFlowType)

  //
  override def genGraph(state: State): Graph = graph.getFlowGraph(state)

  //  override def modules: Map[String, ActorRef] = dependOn

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
      sender() ! state
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
        updateState(event)
        if (cmd.trigger) {
          makeDecision()
        }
        sender() ! state // 返回流程状态
      }

    case CommandHijack(_, updatePoints, updateDecision, trigger) =>

      persist(Hijacked(updatePoints, updateDecision)) { event =>
        updateState(event)
        if (trigger) {
          makeDecision()
        }
        sender() ! state
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

        log.info(s"arrow is ${arrow}!!!!!!!!!!!!!!!!!!!!!!!!!!")
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
              if (edges(graph.getFlowType)(e).check(state)) {
                // 继续调度下一个节点,  maybe, 下一个节点不需要采集新的要素
                log.info(s"continue...")
                makeDecision()
              } else {
                log.info(s"schedule ${e}")
                edges(graph.getFlowType)(e).schedule(state, modules)
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

