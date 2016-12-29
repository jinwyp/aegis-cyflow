package com.yimei.cflow

import com.yimei.cflow.api.models.graph.{GraphConfig, GraphConfigProtocol}
import spray.json._

import scala.io.Source

/**
  * Created by hary on 16/12/29.
  */

//
// todo xj
// 生成类似于类路径下 template目录下的整个目录
//
//
object GenModule extends App with GraphConfigProtocol {

  val classLoader = this.getClass.getClassLoader;

  var graphConfig = Source.fromInputStream(classLoader.getResourceAsStream("ying.json"))
    .mkString
    .parseJson
    .convertTo[GraphConfig]

  println(graphConfig)

  //
  //     aegis-flow-ying/sfdasdfafas   /tmp
  //  aegis-flow-ying.tar.gz           /tmp  aegis-flow-ying.tar.gz
  //

}
