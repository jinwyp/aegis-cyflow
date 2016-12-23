package com.yimei.cflow.test

import com.yimei.cflow.api.util.PointUtil
import spray.json._


case class Color(name: String, red: Int, green: Int, blue: Int)

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val colorFormat = jsonFormat4(Color)
}

/**
  * Created by hary on 16/12/15.
  */
object JsonTest extends App {

  import MyJsonProtocol._
  import PointUtil._

  val color = Color("CadetBlue", 95, 158, 160)
  println(s"color str is ${color.str}")
  println(s"color as[Color] is ${color.str.as[Color]}")

  val k = color.wrap()
  println(s"wrapped is $k")

  val un = k.unwrap
  println(s"wrapped is $un")

}
