package com.yimei.cflow.api.http.models

import com.yimei.cflow.graph.cang.exception.BusinessException
import spray.json
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat}

/**
  * Created by wangqi on 16/12/27.
  */
object ResultModel {

  case class Result[T](data: Option[T], success: Boolean = true, error: Option[Error] = None, meta: Option[PagerInfo]=None )
  case class PagerInfo(total:Int, count:Int, offset:Int, page:Int)
  case class Error(code:Int,message:String,field:String)

  trait ResultProtocol extends DefaultJsonProtocol {

//    implicit object PagerInfoFormat extends RootJsonFormat[PagerInfo] {
//      override def write(obj: PagerInfo): JsValue = {
//        if (obj==null){
//          JsString("")
//        } else {
//          JsObject(("total", JsNumber(obj.total)) , ("count", JsNumber(obj.count)),  ("offset", JsNumber(obj.offset)), ("page",JsNumber(obj.page)))
//        }
//      }
//
//      override def read(json: JsValue): PagerInfo = json match {
//        case JsArray(Vector(JsNumber(total),JsNumber(count),JsNumber(offset),JsNumber(page))) =>
//          PagerInfo(total.toInt,count.toInt,offset.toInt,page.toInt)
//        case _ =>throw new BusinessException("Meta 序列化错误")
//      }
//    }
//
//    implicit object errFormat extends RootJsonFormat[Error] {
//      override def write(obj: Error): JsValue = {
//        if(obj==null){
//          JsString("")
//        } else {
//          JsObject(("code",JsNumber(obj.code)),("message",JsString(obj.message)),("field",JsString(obj.field)))
//        }
//      }
//
//      override def read(json: JsValue): Error =json match {
//        case JsArray(Vector(JsNumber(code),JsString(message),JsString(field))) =>
//          Error(code.toInt,message,field)
//        case _ =>throw new BusinessException("error 序列化错误")
//      }
//    }

    implicit def metaFormat = jsonFormat4(PagerInfo)
    implicit def errorFormat = jsonFormat3(Error)
    implicit def resultEntityFormat[A :JsonFormat] = jsonFormat4(Result.apply[A])
  }

}
