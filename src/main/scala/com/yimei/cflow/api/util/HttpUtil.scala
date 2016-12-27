package com.yimei.cflow.api.util

import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, _}
import akka.util.ByteString
import com.yimei.cflow.config.{ApplicationConfig, CoreConfig}
import com.yimei.cflow.graph.cang.exception.BusinessException
import scala.concurrent.Future
import scala.concurrent.duration._
import HttpMethods._
import akka.http.scaladsl.model._
import com.yimei.cflow.config.CoreConfig


/**
  * Created by wangqi on 16/12/26.
  */

object HttpUtil extends CoreConfig with ApplicationConfig{

  implicit val log: LoggingAdapter = Logging(coreSystem, getClass)
  //发送报文,并取得回复
  def sendRequest(path:String,paramters:Map[String,String] = Map(),pathVariables:Array[String]=Array(),
                  bodyEntity :Option[String] = None , method:String="get"): Future[String] = {

    val httpMethod = method.toLowerCase match {
      case "get" => HttpMethods.GET
      case "post" => HttpMethods.POST
      case "put" => HttpMethods.PUT
      case "delete" => HttpMethods.DELETE
      case _  => throw new BusinessException("传入http方法有误")
    }

    val pathWithPathVariables = pathVariables.foldLeft(path)((p,pvb)=>p+"/"+pvb)
    val paras = paramters.foldLeft("?")((p,pts)=>p+pts._1+"="+pts._2+"&")

    val fullUrl = paras.isEmpty match {
      case true => url + pathWithPathVariables
      case false => url + pathWithPathVariables + paras.substring(0,paras.length-1)
    }

    log.info("request url: {}, request body: {}",fullUrl,bodyEntity.getOrElse("Empty"))
    import MediaTypes._


  //  val contentType = headers.`Content-Type`.apply(ContentType(`application/json`))

    val httpRequest = bodyEntity match {
      case Some(a) => HttpRequest(uri =fullUrl , entity = HttpEntity(`application/json`,ByteString(a,"UTF-8")),method = httpMethod)
      case _       => HttpRequest(uri =fullUrl ,method = httpMethod)
    }


    //发送请求,并得到结果
    Http().singleRequest(
      httpRequest
    ) flatMap { r =>


      val strictEntity = r.entity.toStrict(10.seconds)
      val byteString: Future[ByteString] = strictEntity flatMap { e =>
        e.dataBytes
          .runFold(ByteString.empty) { case (acc, b) => acc ++ b }
      }
      val result = byteString map(_.decodeString("UTF-8"))

      r.status match {
        case StatusCodes.OK =>
          result
        case _            =>
          result map( r => throw new BusinessException(r))
      }
    } recover {
      case BusinessException(r) =>
        log.info(r)
        throw new BusinessException(r)
      case _ =>
        log.error("!!!!!!!!!!!!!")
        throw new BusinessException("网络异常")
    }
  }

}
