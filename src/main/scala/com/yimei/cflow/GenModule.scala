package com.yimei.cflow

import java.io.{File, PrintWriter}

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

  val classLoader = this.getClass.getClassLoader

  var graphConfigStr = Source.fromInputStream(classLoader.getResourceAsStream("ying.json")).mkString
  var graphConfig = graphConfigStr.parseJson.convertTo[GraphConfig]

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
  var templateDir = "./template"
  val graphJarStr = graphConfig.graphJar
  val nameArray: Array[String] = graphJarStr.split('.')
  var directoryName = "/" + nameArray(nameArray.length - 2)
  var jarName = nameArray(nameArray.length - 1)
  var jarDirName = "/" + graphJarStr.substring(0, graphJarStr.length - directoryName.length - jarName.length - 1).replace(".", "-")

  val buildPropertiesContent = Source.fromInputStream(classLoader.getResourceAsStream(templateDir + projectDir + buildProperties)).mkString
  val pluginsSbtContent = Source.fromInputStream(classLoader.getResourceAsStream(templateDir + projectDir + pluginsSbt)).mkString
  var configScalaContent = "\nobject Config {\n"
  configScalaContent += "\n\t// points\n"
  graphConfig.points.toList.sortBy(p => p._1).foreach(p =>
    configScalaContent += "\tval point_" + p._1 + "=\"" + p._1 + "\"\t\t\t//" + p._2 + "\n"
  )
  configScalaContent += "\n\t// vertices\n"
  graphConfig.vertices.toList.sortBy(v => v._1).foreach(v =>
    configScalaContent += "\tval vertex_" + v._1 + "=\"" + v._1 + "\"\t\t\t//" + v._2 + "\n"
  )
  configScalaContent += "\n\t// autoTasks\n"
  graphConfig.autoTasks.toList.sortBy(a => a._1).foreach(a =>
    configScalaContent += "\tval auto_" + a._1 + "=\"" + a._1 + "\"\t\t\t//" + a._2 + "\n"
  )
  configScalaContent += "\n\t// userTasks\n"
  graphConfig.userTasks.toList.sortBy(u => u._1).foreach(u =>
    configScalaContent += "\tval task_" + u._1 + "=\"" + u._1 + "\"\t\t\t//" + u._2 + "\n"
  )
  configScalaContent += "}\n"

  var file: File = new File(rootDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + projectDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + projectDir + buildProperties)
  val pw_build = new PrintWriter(file)
  pw_build.write(buildPropertiesContent)
  pw_build.close
  file = new File(rootDir + directoryName + projectDir + pluginsSbt)
  val pw_plugins = new PrintWriter(file)
  pw_plugins.write(pluginsSbtContent)
  pw_plugins.close
  file = new File(rootDir + directoryName + srcDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + resourceDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + resourceDir + flowJson)
  val pw_flow = new PrintWriter(file)
  pw_flow.write(graphConfigStr)
  pw_flow.close
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName + directoryName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName + directoryName + configScala)
  val pw_config = new PrintWriter(file)
  pw_config.write(configScalaContent)
  pw_config.close
  file = new File(rootDir + directoryName + srcDir + mainDir + scalaDir + jarDirName + directoryName + templateGraphJarScala)
  val pw_templateGraphJar = new PrintWriter(file)
  pw_templateGraphJar.write("")
  pw_templateGraphJar.close



  //
  //     aegis-flow-ying/sfdasdfafas   /tmp
  //  aegis-flow-ying.tar.gz           /tmp  aegis-flow-ying.tar.gz
  //

}
