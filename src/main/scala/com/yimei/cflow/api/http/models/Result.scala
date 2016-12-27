package com.yimei.cflow.api.http.models

import spray.json.{DefaultJsonProtocol, JsonFormat}

/**
  * Created by wangqi on 16/12/27.
  */
object ResultModel {

  case class Result[T](data: T, success: Boolean = true, error: Error  = null, meta:Meta = null)
  case class Meta(total:Int, count:Int, offset:Int, page:Int)
  case class Error(code:Int,message:String,field:String)

  trait ResultProtocol extends DefaultJsonProtocol {
    implicit def metaFormat = jsonFormat4(Meta)
    implicit def errorFormat = jsonFormat3(Error)
    implicit def resultEntityFormat[A :JsonFormat] = jsonFormat4(Result.apply[A])
  }

}
