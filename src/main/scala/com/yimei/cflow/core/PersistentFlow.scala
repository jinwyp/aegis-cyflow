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

class PersistentFlow(
                      val graph: FlowGraph,
                      flowId: String,
                      modules: Map[String, ActorRef],
                      pid: String,
                      guid: String,
                      initData: Map[String, String]
                    ) extends AbstractFlow with PersistentActor {

  import Flow._

  override def persistenceId: String = pid

  // 钝化超时时间
  val timeout = graph.timeout

  log.info(s"timeout is $timeout")
  context.setReceiveTimeout(timeout seconds)

  // 流程初始化数据
  val initPoints = initData.map { entry =>
    (entry._1, DataPoint(entry._2, None, None, "init", new Date().getTime, false))
  }

  override var state = State(flowId, guid, initPoints, Map("start" -> true), Nil, graph.flowType)

  //
  override def genGraph(state: State): Graph = graph.graph(state)

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
      makeDecision(state.edges.keys)  // 用当前的edges开始决策!!!!

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
          val tocheck = points.keys.map(graph.pointEdges(_)).toSet.toIterable
          makeDecision(tocheck)
        }
        sender() ! state // 返回流程状态
      }

    case CommandHijack(_, updatePoints, updateDecision, trigger) =>

      persist(Hijacked(updatePoints, updateDecision)) { event =>
        updateState(event)
        if (trigger) {
          val tocheck = updatePoints.keys.map(graph.pointEdges(_)).toSet
          makeDecision(tocheck)
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

  protected def processCommandPoint(cmd: CommandPoint) = {
    persist(PointUpdated(cmd.name, cmd.point)) {
      event =>
        log.info(s"${event} persisted")
        updateState(event)
        makeDecision(Seq(graph.pointEdges(cmd.name)))
    }
  }

  protected def processCommandPoints(cmds: CommandPoints) = {
    persist(PointsUpdated(cmds.points)) {
      event =>
        log.info(s"${event} persisted")
        updateState(event)
        val tocheck = cmds.points.keys.map(graph.pointEdges(_)).toSet
        makeDecision(tocheck)
    }
  }

  /**
    * 所有可以决策的边
    *
    * @param edgeNames
    */
  protected def makeDecision(edgeNames: Iterable[String]) = {
    edgeNames.foreach { name =>
      val e = graph.edges(name);
      if (e.check((state))) {
        persist(EdgeCompleted(name)) { event =>
          updateState(event)
          make(e)
        }
      }
    }
  }

  /**
    *  对每条决策边,
    *
    * @param e
    */
  protected def make(e: Edge): Unit = {
    val arrows: Seq[Arrow] = graph.deciders(e.end)(state)

    persist(DecisionUpdated(e.end, arrows)) { event =>

      updateState(event)

      arrows.foreach { arr =>
        arr match {
          case a@Arrow(j, Some(e)) =>
            graph.edges(e).schedule(state, modules)   // 这个决策返回边是调度边, 则调度!!!
            logState(s"$a")

          case arrow@Arrow(FlowSuccess, None) =>
            logState("FlowSuccess")

          case arrow@Arrow(FlowFail, None) =>
            logState("FlowFail")
        }
      }
    }
  }
}
