package com.yimei.cflow

import java.io._
import java.util.zip.GZIPOutputStream

import com.yimei.cflow.api.models.graph.{GraphConfig, GraphConfigProtocol}
import org.apache.commons.compress.utils.IOUtils
import spray.json._

import scala.io.Source

/**
  * Created by hary on 16/12/29.
  */
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
  val scalaDir = "/scala"
  val configScala = "/Config.scala"
  val templateGraphJarScala = "/TemplateGraphJar.scala"
  var templateDir = "./template"
  val projectName = "aegis-flow-" + graphConfig.artifact
  val projectNameDir = "/" + projectName
  val jarName = graphConfig.entry
  val jarDirName = "/" + graphConfig.groupId.replace(".", "-")

  val buildPropertiesContent = Source.fromInputStream(classLoader.getResourceAsStream(templateDir + projectDir + buildProperties)).mkString
  val pluginsSbtContent = Source.fromInputStream(classLoader.getResourceAsStream(templateDir + projectDir + pluginsSbt)).mkString
  var configScalaContent = "\nobject Config {\n"
  configScalaContent += "\n\t// points\n"
  graphConfig.points.toList.sortBy(p => p._1).foreach(p =>
    configScalaContent += "\tval point_" + p._1 + " = \"" + p._1 + "\"\t\t\t//" + p._2 + "\n"
  )
  configScalaContent += "\n\t// vertices\n"
  graphConfig.vertices.toList.sortBy(v => v._1).foreach(v =>
    configScalaContent += "\tval vertex_" + v._1 + " = \"" + v._1 + "\"\t\t\t//" + v._2 + "\n"
  )
  configScalaContent += "\n\t// autoTasks\n"
  graphConfig.autoTasks.toList.sortBy(a => a._1).foreach(a =>
    configScalaContent += "\tval auto_" + a._1 + " = \"" + a._1 + "\"\t\t\t//" + a._2 + "\n"
  )
  configScalaContent += "\n\t// userTasks\n"
  graphConfig.userTasks.toList.sortBy(u => u._1).foreach(u =>
    configScalaContent += "\tval task_" + u._1 + " = \"" + u._1 + "\"\t\t\t//" + u._2 + "\n"
  )
  configScalaContent += "}\n"

  var templateGraphJarScalaContent = "\nobject TemplateGraphJar {\n"
  templateGraphJarScalaContent += "\n\t// 决策点\n"
  graphConfig.vertices.toList.sortBy(v => v._1).foreach(v => {
    templateGraphJarScalaContent += "\t@Description(\"" + v._2 + "\")\n"
    templateGraphJarScalaContent += "\tdef " + v._1 + "(state: State): Seq[Arrow]  = ???\n\n"
  })
  templateGraphJarScalaContent += "\n\t// 自动任务\n"
  graphConfig.autoTasks.toList.sortBy(a => a._1).foreach(a => {
    templateGraphJarScalaContent += "\t@Description(\"" + a._2 + "\")\n"
    templateGraphJarScalaContent += "\tdef " + a._1 + "(task: CommandAutoTask): Future[Map[String, String]] = ???\n\n"
  })
  templateGraphJarScalaContent += "\n\t// 任务路由 get\n"
  graphConfig.userTasks.toList.sortBy(u => u._1).foreach(u => {
    templateGraphJarScalaContent += "\t@Description(\"" + u._2 + "\")\n"
    templateGraphJarScalaContent += "\tdef get" + u._1 + "(proxy: ActorRef): Route = ???\n\n"
  })
  templateGraphJarScalaContent += "\n\t// 任务路由 post\n"
  graphConfig.userTasks.toList.sortBy(u => u._1).foreach(u => {
    templateGraphJarScalaContent += "\t@Description(\"" + u._2 + "\")\n"
    templateGraphJarScalaContent += "\tdef post" + u._1 + "(proxy: ActorRef): Route = ???\n\n"
  })
  templateGraphJarScalaContent += "}\n"

  var file: File = new File(rootDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + projectDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + projectDir + buildProperties)
  val pw_build = new PrintWriter(file)
  pw_build.write(buildPropertiesContent)
  pw_build.close
  file = new File(rootDir + projectNameDir + projectDir + pluginsSbt)
  val pw_plugins = new PrintWriter(file)
  pw_plugins.write(pluginsSbtContent)
  pw_plugins.close
  file = new File(rootDir + projectNameDir + srcDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + srcDir + mainDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + srcDir + mainDir + resourceDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + srcDir + mainDir + resourceDir + flowJson)
  val pw_flow = new PrintWriter(file)
  pw_flow.write(graphConfigStr)
  pw_flow.close
  file = new File(rootDir + projectNameDir + srcDir + mainDir + scalaDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + srcDir + mainDir + scalaDir + jarDirName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectNameDir + srcDir + mainDir + scalaDir + jarDirName + configScala)
  val pw_config = new PrintWriter(file)
  pw_config.write(configScalaContent)
  pw_config.close
  file = new File(rootDir + projectNameDir + srcDir + mainDir + scalaDir + jarDirName + templateGraphJarScala)
  val pw_templateGraphJar = new PrintWriter(file)
  pw_templateGraphJar.write(templateGraphJarScalaContent)
  pw_templateGraphJar.close

  val destDir = new File(rootDir + projectNameDir)

  import org.apache.commons.compress.archivers.tar.{TarArchiveEntry, TarArchiveOutputStream}

  val fos: FileOutputStream = new FileOutputStream(rootDir + projectNameDir + ".tar.gz")
  val tos: TarArchiveOutputStream = new TarArchiveOutputStream(new GZIPOutputStream(new BufferedOutputStream(fos)))
  tos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR)
  tos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
  addFileToCompression(tos, destDir, ".")
  tos.close()
  fos.close()

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
