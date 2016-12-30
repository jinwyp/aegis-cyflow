package com.yimei.cflow

import java.io.File

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

  val rootDir = "./tmp"
  val projectDir = "/project"
  val buildProperties = "/build.properties"
  val pluginsSbt = "/plugins.sbt"
  val srcDir = "/src"
  val mainDir = "/main"
  val resourceDir = "/resources"
  val flowJson = "/flow.json"
  val buildSbt = "/build.sbt"
  val configScala = "/Config.scala"
  val templateGraphJarScala = "/TemplateGraphJar.scala"
  val scalaDir = "/scala"
  var file: File = new File(rootDir)
  if (!file.exists()) file.mkdir()

  println(graphConfig.graphJar)
  val graphJarStr = graphConfig.graphJar
  val nameArray: Array[String] = graphJarStr.split('.')
  var directoryName = "/" + nameArray(nameArray.length - 2)
  var jarName = nameArray(nameArray.length - 1)
  var jarDirName = "/" + graphJarStr.substring(0, graphJarStr.length - directoryName.length - jarName.length - 1).replace(".", "-")
  file = new File(rootDir + directoryName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + projectDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + projectDir + buildProperties)
  if (!file.exists()) file.createNewFile()
  file = new File(rootDir + directoryName + projectDir + pluginsSbt)
  if (!file.exists()) file.createNewFile()
  file = new File(rootDir + directoryName + srcDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + resourceDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + resourceDir + flowJson)
  if (!file.exists()) file.createNewFile()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName + directoryName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName + directoryName + configScala)
  if (!file.exists()) file.createNewFile()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName + directoryName + templateGraphJarScala)
  if (!file.exists()) file.createNewFile()



  //
  //     aegis-flow-ying/sfdasdfafas   /tmp
  //  aegis-flow-ying.tar.gz           /tmp  aegis-flow-ying.tar.gz
  //

}
