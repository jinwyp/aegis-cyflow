package com.yimei.cflow.util

import spray.json._

object Implicits {
  /**
    * Created by hary on 16/12/15.
    */
  implicit class str2object(str: String) {
    def as[A: JsonFormat] = str.parseJson.convertTo[A]
  }

  implicit class object2str[A:JsonFormat](o: A){
    def str = o.toJson.toString()
  }
}
