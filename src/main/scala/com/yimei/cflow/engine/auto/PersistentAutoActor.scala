package com.yimei.cflow.engine.auto

import java.util.UUID

import akka.actor.{ActorLogging, ActorRef}
import akka.persistence.PersistentActor
import com.yimei.cflow.api.models.flow.{CommandPoints, DataPoint}
import com.yimei.cflow.config.GlobalConfig._
import com.yimei.cflow.engine.auto.AutoMaster.CommandAutoTask

import scala.concurrent.Future


/**
  * todo
  *  如何保证幂等性  ???
  *  只能通过记流水的方式才能完成
  * Created by hary on 16/12/28.
  */
class PersistentAutoActor(
                           name: String,
                           modules: Map[String, ActorRef],
                           auto: (CommandAutoTask) => Future[Map[String, String]]
                         )
  extends PersistentActor with ActorLogging {

  override def persistenceId: String = name;

  import scala.concurrent.ExecutionContext
  import ExecutionContext.Implicits.global

  override def receiveRecover: Receive = ???


  override def receiveCommand: Receive = {
    case task: CommandAutoTask =>

      // 1> 先从数据库查流水, 如果查到结果, 就直接回复flow

      // 2> 如果流水中没有结果, 则记录事件, 发起外部任务,

      // 3> 外部任务返回更新数据库记录

      persistAsync(task) { event =>
        auto(task).map { values =>
          modules(module_flow) ! CommandPoints(
            task.state.flowId,
            values.map { entry =>
              ((entry._1) -> DataPoint(entry._2, None, Some(name), UUID.randomUUID().toString, 10, false))
            }
          )
        }
      }
  }
}

