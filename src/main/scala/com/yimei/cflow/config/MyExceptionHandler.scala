package com.yimei.cflow.config

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.graph.cang.exception.BusinessException
import spray.json._

/**
  * Created by wangqi on 16/12/27.
  */
trait MyExceptionHandler extends ResultProtocol with SprayJsonSupport {

  this: {val coreSystem:ActorSystem} =>


  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)

  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e:BusinessException =>
      extractUri { uri =>
        log.error(s"Request to $uri could not be handled normally!!!!!!!!! BusinessException")
        complete(HttpResponse(StatusCodes.BadRequest,entity = Result(data = Some("error"),success = false,error = Error(409,e.message,"")).toJson.toString ))
      }

    case e:DatabaseException =>
      extractUri { uri =>
        println(s"Request to $uri could not be handled normally!!!!!!!!! DatabaseException11111111 {}",e.message)
        complete(HttpResponse(StatusCodes.BadRequest,entity = e.message ))
      }
    case e =>
      extractUri { uri =>
        log.error(s"Request to $uri could not be handled normally!!!!!!!!!")
        log.error("{}",e)
        complete(HttpResponse(StatusCodes.InternalServerError,entity = Result(data = Some("error"),success = false,error = Error(500,"系统错误","")).toJson.toString ))
      }
  }
}
