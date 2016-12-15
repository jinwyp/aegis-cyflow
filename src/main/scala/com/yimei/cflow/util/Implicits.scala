package com.yimei.cflow.util

import spray.json._

object Implicits {
  /**
    * Created by hary on 16/12/15.
    */
  implicit class Implicits(str: String) {
    def as[A: JsonFormat] = str.parseJson.convertTo[A]
  }
}
