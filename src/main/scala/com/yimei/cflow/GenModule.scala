package com.yimei.cflow

import java.io._
import java.util.zip.GZIPOutputStream

import com.yimei.cflow.api.models.graph.{GraphConfig, GraphConfigProtocol}
import org.apache.commons.compress.utils.IOUtils
import spray.json._

import scala.io.Source

object GenModule extends App with GraphConfigProtocol {

  val classLoader = this.getClass.getClassLoader

  var graphConfigStr = Source.fromInputStream(classLoader.getResourceAsStream("ying.json")).mkString
  var graphConfig = graphConfigStr.parseJson.convertTo[GraphConfig]

  // 文件目录
  val rootDirName = "tmp"
  val projectName = "aegis-flow-" + graphConfig.artifact
  val buildPropertiesDir = Array(rootDirName, projectName, "project")
  val buildProperties =  "build.properties"
  val pluginsSbtFile = "plugins.sbt"
  val flowJsonFileDir = Array(rootDirName, projectName, "src", "main", "resources")
  val flowJsonFile = "flow.json"
  val projectPackages = (graphConfig.groupId + "." + graphConfig.artifact).split('.')
  var configScalaFileDir = Array(rootDirName, projectName, "src", "main", "scala")
  configScalaFileDir ++= projectPackages
  val configScalaFile = "Config.scala"
  val graphJarScalaFile = graphConfig.entry + ".scala"

  var templateDir = "./template"
  val buildPropertiesContent = Source.fromInputStream(classLoader.getResourceAsStream(templateDir + "/project/" + buildProperties)).mkString
  val pluginsSbtContent = Source.fromInputStream(classLoader.getResourceAsStream(templateDir + "/project/" + pluginsSbtFile)).mkString

  // Config.scala 文件内容
  var configScalaContent =
    "\n" +
      "object Config {" +
      "\n"
  val configElementList: Map[Array[String], Map[String, Serializable]] = Map(
    Array("points", "val point_") -> graphConfig.points,
    Array("vertices", "val vertex_") -> graphConfig.vertices,
    Array("autoTasks", "val auto_") -> graphConfig.autoTasks,
    Array("userTasks", "val task_") -> graphConfig.userTasks
  )
  configElementList.foreach(o => configScalaContent += createConfigContent(o._1, o._2))
  configScalaContent += "}\n"

  // GraphJar.scala 文件内容
  var graphJarScalaContent =
    "\n" +
      "object " + graphConfig.entry + " {" +
      "\n"
  val graphJarElementList: Map[Array[String], Map[String, Serializable]] = Map(
    Array("决策点", "@Description(", "def ", "(state: State): Seq[Arrow] = ???") -> graphConfig.vertices,
    Array("自动任务", "@Description(", "def ", "(task: CommandAutoTask): Future[Map[String, String]] = ???") -> graphConfig.autoTasks,
    Array("任务路由 get", "@Description(", "def get", "(proxy: ActorRef): Route = ???") -> graphConfig.userTasks,
    Array("任务路由 post", "@Description(", "def post", "(proxy: ActorRef): Route = ???") -> graphConfig.userTasks
  )
  graphJarElementList.foreach(o => graphJarScalaContent += createGraphJarContent(o._1, o._2))
  graphJarScalaContent += "}\n"

  // 创建文件夹,文件
  createDynamicFile(buildPropertiesDir, buildProperties, buildPropertiesContent)
  createDynamicFile(buildPropertiesDir, pluginsSbtFile, pluginsSbtContent)
  createDynamicFile(flowJsonFileDir, flowJsonFile, graphConfigStr)
  createDynamicFile(configScalaFileDir, configScalaFile, configScalaContent)
  createDynamicFile(configScalaFileDir, graphJarScalaFile, graphJarScalaContent)

  // 生成 tar.gz 文件
  val destDir = new File(rootDirName + "/" + projectName)

  import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream}

  val fos: FileOutputStream = new FileOutputStream(rootDirName + "/" + projectName + ".tar.gz")
  val tos: TarArchiveOutputStream = new TarArchiveOutputStream(new GZIPOutputStream(new BufferedOutputStream(fos)))
  tos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR)
  tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
  addFileToCompression(tos, destDir, ".")
  tos.close()
  fos.close()

  // 生成 Config.scala 文件内容
  def createConfigContent(strArray: Array[String], map: Map[String, Serializable]): String = {
    var result = "\n\t// " + strArray(0) + "\n"
    map.toList.sortBy(o => o._1).foreach(o =>
      result += "\t" + strArray(1) + o._1 + " = \"" + o._1 + "\"\t\t\t//" + o._2 + "\n"
    )
    result
  }

  // 生成 GraphJar.scala 文件内容
  def createGraphJarContent(strArray: Array[String], map: Map[String, Serializable]): String = {
    var result = "\n\t// " + strArray(0) + "\n"
    map.toList.sortBy(o => o._1).foreach(o => {
      result += "\t" + strArray(1) + "(\"" + o._2 + "\")\n"
      result += "\t" + strArray(2) + o._1 + strArray(3) + "\n\n"
    })
    result
  }

  // 创建动态文件目录
  def createDynamicDir(names: Array[String]): String = {
    var path: String = "."
    names.foreach(name => {
      path += "/" + name
      val file = new File(path)
      if (!file.exists()) file.mkdir()
    })
    path
  }

  // 创建文件
  def createDynamicFile(names: Array[String], fileName: String, fileContent: String): Unit = {
    val path = createDynamicDir(names)
    val file = new File(path + "/" + fileName)
    val pw = new PrintWriter(file)
    pw.write(fileContent)
    pw.close
  }

  // 生成 tar.gz 文件
  def addFileToCompression(tos: TarArchiveOutputStream, file: File, dir: String) {
    val tae: TarArchiveEntry = new TarArchiveEntry(file, dir)
    tos.putArchiveEntry(tae)
    if (file.isDirectory()) {
      tos.closeArchiveEntry()
      file.listFiles().foreach(childFile =>
        addFileToCompression(tos, childFile, dir + "/" + childFile.getName())
      )
    } else {
      val fis: FileInputStream = new FileInputStream(file)
      IOUtils.copy(fis, tos)
      tos.flush()
      tos.closeArchiveEntry()
    }
  }

}
