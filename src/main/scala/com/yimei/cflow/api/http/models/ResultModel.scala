package com.yimei.cflow.api.http.models

import com.yimei.cflow.graph.cang.exception.BusinessException
import spray.json
import spray.json.{DefaultJsonProtocol, JsArray, JsNumber, JsObject, JsString, JsValue, JsonFormat, RootJsonFormat}

/**
  * Created by wangqi on 16/12/27.
  */
object ResultModel {

  case class Result[T](data: Option[T], success: Boolean = true, error: Error  = null, meta:Meta = null)
  case class Meta(total:Int, count:Int, offset:Int, page:Int)
  case class Error(code:Int,message:String,field:String)

  trait ResultProtocol extends DefaultJsonProtocol {

    implicit object MetaFormat extends RootJsonFormat[Meta] {
      override def write(obj: Meta): JsValue = {
        if (obj==null){
          JsString("")
        } else {
          JsArray(JsNumber(obj.total),JsNumber(obj.count),JsNumber(obj.offset),JsNumber(obj.page))
        }
      }

      override def read(json: JsValue): Meta = json match {
        case JsArray(Vector(JsNumber(total),JsNumber(count),JsNumber(offset),JsNumber(page))) =>
          Meta(total.toInt,count.toInt,offset.toInt,page.toInt)
        case _ =>throw new BusinessException("Meta 序列化错误")
      }
    }

    implicit object errFormat extends RootJsonFormat[Error] {
      override def write(obj: Error): JsValue = {
        if(obj==null){
          JsString("")
        } else {
          JsArray(JsNumber(obj.code),JsString(obj.message),JsString(obj.field))
        }
      }

      override def read(json: JsValue): Error =json match {
        case JsArray(Vector(JsNumber(code),JsString(message),JsString(field))) =>
          Error(code.toInt,message,field)
        case _ =>throw new BusinessException("error 序列化错误")
      }
    }

    //implicit def metaFormat = jsonFormat4(Meta)
    //implicit def errorFormat = jsonFormat3(Error)
    implicit def resultEntityFormat[A :JsonFormat] = jsonFormat4(Result.apply[A])
  }

}