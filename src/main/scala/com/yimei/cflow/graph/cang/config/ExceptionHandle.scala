package com.yimei.cflow.graph.cang.config

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{complete, extractUri}
import akka.http.scaladsl.server.ExceptionHandler
import com.yimei.cflow.api.http.models.ResultModel.{Error, Result, ResultProtocol}
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.config.CoreConfig._
import spray.json._

/**
  * Created by wangqi on 16/12/27.
  */

trait ExceptionHandle extends ResultProtocol with SprayJsonSupport with Config{



  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)

  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e:BusinessException =>
      extractUri { uri =>
        log.error(s"Request to $uri could not be handled normally!!!!!!!!! BusinessException")
        complete(HttpResponse(StatusCodes.BadRequest,entity = Result[String](data = None,success = false,error = Some(Error(409,e.message,""))).toJson.toString ))
      }

    case e =>
      extractUri { uri =>
        log.error(s"Request to $uri could not be handled normally!!!!!!!!!")
        log.error("{}",e)
        complete(HttpResponse(StatusCodes.InternalServerError,entity = Result(data = Some("error"),success = false,error = Some(Error(500,"系统错误",""))).toJson.toString ))
      }
  }
}
