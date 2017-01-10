package com.yimei.cflow.api.util

import akka.event.{Logging, LoggingAdapter}
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.config.DatabaseConfig._

import scala.concurrent.Future
import com.yimei.cflow.config.CoreConfig._

/**
  * Created by wangqi on 16/12/16.
  */
object DBUtils {
  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)

  import driver.api._
  import slick.dbio.DBIOAction
  //封装数据库操作
  def dbrun[R](a: DBIOAction[R, NoStream, Nothing]): Future[R] ={
    val result = db.run(a)
//    result onFailure {
//      case a:SQLIntegrityConstraintViolationException => log.warning("该记录已存在")
//      case a => {log.info("database err: {}",a); throw new DatabaseException(a.getMessage)}
//    }
    result
  }



}
