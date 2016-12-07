package com.yimei.cflow.core

import akka.actor.ReceiveTimeout
import akka.persistence.{PersistentActor, RecoveryCompleted, SaveSnapshotSuccess, SnapshotOffer}
import com.yimei.cflow.integration.DependentModule

import scala.concurrent.duration._

/**
  * Created by hary on 16/12/6.
  */
abstract class PersistentFlow(passivateTimeout: Long) extends AbstractFlow
  with DependentModule
  with PersistentActor {

  import Flow._

  // 钝化超时时间
  context.setReceiveTimeout(passivateTimeout seconds)

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
      sender() ! RunFlowSuccess(flowId)
      makeDecision

    case cmd: CommandPoint =>
      log.info(s"received ${cmd.name}")
      processCommandPoint(cmd)

    case cmds: CommandPoints =>
      log.info(s"received ${cmds.points.map(_._1)}")
      processCommandPoints(cmds)

    // received 超时
    case ReceiveTimeout =>
      log.info(s"passivate timeout, begin passivating!!!!")
      context.stop(self)

    case SaveSnapshotSuccess(metadata) =>
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

    cur.run(state) match {
      case j: Judge =>
        logState("before judge")
        persist(DecisionUpdated(j)) {
          event =>
            log.info(s"${event} persisted")
            updateState(event)
            logState("after judge")
            log.info(s"check ${j.in}")

            // circular
            if (cur == j) {
              // clear the points from State where j.in is responsible for
              persist(null: Event) { event =>
                updateState(event) // @todo 王琦
              }
            } else {
              if (j.in.check(state)) {
                // 继续调度下一个节点,  maybe, 下一个节点不需要采集新的要素
                log.info(s"continue...")
                makeDecision()
              } else {
                log.info(s"schedule ${j.in}")
                j.in.schedule(state, modules)
              }
            }
        }
      case FlowSuccess =>
        logState("FlowSuccess")
        persist(DecisionUpdated(FlowSuccess)) {
          event =>
            log.info(s"${event.decision} persisted, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case FlowFail =>
        logState("FlowFail")
        persist(DecisionUpdated(FlowFail)) {
          event =>
            log.info(s"${event.decision} persisted, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case FlowTodo =>
        log.info(s"current decision [${state.decision}] result: FlowTodo")
    }
  }
}
