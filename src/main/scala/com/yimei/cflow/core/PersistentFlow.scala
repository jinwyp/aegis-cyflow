package com.yimei.cflow.core

import akka.actor.ReceiveTimeout
import akka.persistence.{PersistentActor, RecoveryCompleted, SnapshotOffer}
import com.yimei.cflow.integration.DependentModule
import concurrent.duration._

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

  // 命令处理
  def receiveCommand = {
    case k : CommandRunFlow.type =>
      log.info(s"received $k")
      val src = sender()
      persist(UserUpdated(state.userId)) { event =>
        updateState(event)
        src ! CreateFlowSuccess
        log.info(s"persist ${event} success")
        makeDecision
      }

    case cmd: CommandPoint =>
      log.info(s"received ${cmd.name}")
      processCommandPoint(cmd)

    case cmds: CommandPoints =>
      log.info(s"received ${cmds.points.map(_._1)}")
      processCommandPoints(cmds)

    case query: CommandQueryFlow =>
      log.info("received CommandQuery")
      sender() ! FlowGraphJson(queryStatus(state))

    case shutdown: CommandShutdown =>
      log.info("received CommandShutdown")
      context.stop(self)

    // received 超时
    case ReceiveTimeout =>
      log.info(s"passivate timeout, begin passivating!!!!")
      context.stop(self)

    case _ =>
  }

  override def unhandled(msg: Any): Unit = log.error(s"received unhandled message: $msg")

  // 处理命令
  protected def processCommandPoint(cmd: CommandPoint) = {

    persist(PointUpdated(cmd.name, cmd.point)) {
      event =>

        // 更新状体
        log.info(s"persist ${event.name} success")
        updateState(event)

        // 作决定!!!
        makeDecision
    }
  }

  // 处理复合命令
  protected def processCommandPoints(cmds: CommandPoints) = {
    persist(PointsUpdated(cmds.points)) {
      event =>
        log.info(s"persist ${event.pionts.map(_._1)} success")
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
            log.info(s"persist ${event.decision} success")
            updateState(event)
            logState("after judge")
            log.info(s"check ${j.in}")

            // circular
            if ( cur == j) {
              // clear the points from State where j.in is responsible for
              persist(null: Event) { event =>
                updateState(event)   // @todo 王琦
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
            log.info(s"persist ${ event.decision } success, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case FlowFail =>
        logState("FlowFail")
        persist(DecisionUpdated(FlowFail)) {
          event =>
            log.info(s"persist ${ event.decision } success, begin snapshot")
            updateState(event)
            saveSnapshot(state)
        }
      case FlowTodo =>
        log.info(s"current decision [${ state.decision }] result: FlowTodo")
    }
  }
}
