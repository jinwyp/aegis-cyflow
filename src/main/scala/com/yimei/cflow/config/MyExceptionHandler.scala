package com.yimei.cflow.config

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.MediaTypes._
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.util.ByteString
import com.yimei.cflow.api.http.models.ResultModel._
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.graph.cang.exception.BusinessException
import spray.json._
import com.yimei.cflow.config.CoreConfig._
import akka.http.scaladsl.model._

/**
  * Created by wangqi on 16/12/27.
  */
trait MyExceptionHandler extends ResultProtocol with SprayJsonSupport {

//  this: {val coreSystem:ActorSystem} =>


  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)

  implicit def myExceptionHandler: ExceptionHandler = ExceptionHandler {
    case e:BusinessException =>
      extractUri { uri =>
        log.error("Request to {} could not be handled normally!! error:{} ",uri,e)
        complete(HttpResponse(StatusCodes.BadRequest,entity = HttpEntity(`application/json`, ByteString(Result(data = Some("error"),success = false,error = Some(Error(409,e.message,""))).toJson.toString, "UTF-8")) ))
      }

    case e:DatabaseException =>
      extractUri { uri =>
        log.error("Request to {} could not be handled normally!! error:{} ",uri,e)
        complete(HttpResponse(StatusCodes.BadRequest,entity = HttpEntity(`application/json`, ByteString(Result(data = Some("error"),success = false,error = Some(Error(409,e.message,""))).toJson.toString ))))
      }
    case e =>
      extractUri { uri =>
        log.error("Request to {} could not be handled normally!! error:{} ",uri,e)
        complete(HttpResponse(StatusCodes.InternalServerError,entity = HttpEntity(`application/json`, ByteString(Result(data = Some("error"),success = false,error = Some(Error(500,"系统错误",""))).toJson.toString ))))
      }
  }
}
