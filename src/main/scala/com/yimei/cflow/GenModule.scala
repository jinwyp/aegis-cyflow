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
  val projectName = "aegis-flow-" + nameArray(nameArray.length - 2)
  val projectRootDir = "/" + projectName
  val jarName = nameArray(nameArray.length - 1)
  val jarDirName = "/" + graphJarStr.substring(0, graphJarStr.length - nameArray(nameArray.length - 2).length - jarName.length - 2).replace(".", "-")

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
    templateGraphJarScalaContent += "\t@Description(\"" + v._2 +"\")\n"
    templateGraphJarScalaContent += "\tdef " + v._1 + "(state: State): Seq[Arrow]  = ???\n\n"
  })
  templateGraphJarScalaContent += "\n\t// 自动任务\n"
  graphConfig.autoTasks.toList.sortBy(a => a._1).foreach(a => {
    templateGraphJarScalaContent += "\t@Description(\"" + a._2 +"\")\n"
    templateGraphJarScalaContent += "\tdef " + a._1 + "(task: CommandAutoTask): Future[Map[String, String]] = ???\n\n"
  })
  templateGraphJarScalaContent += "\n\t// 任务路由 get\n"
  graphConfig.userTasks.toList.sortBy(u => u._1).foreach(u => {
    templateGraphJarScalaContent += "\t@Description(\"" + u._2 +"\")\n"
    templateGraphJarScalaContent += "\tdef get" + u._1 + "(proxy: ActorRef): Route = ???\n\n"
  })
  templateGraphJarScalaContent += "\n\t// 任务路由 post\n"
  graphConfig.userTasks.toList.sortBy(u => u._1).foreach(u => {
    templateGraphJarScalaContent += "\t@Description(\"" + u._2 +"\")\n"
    templateGraphJarScalaContent += "\tdef post" + u._1 + "(proxy: ActorRef): Route = ???\n\n"
  })
  templateGraphJarScalaContent += "}\n"

  var file: File = new File(rootDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + projectDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + projectDir + buildProperties)
  val pw_build = new PrintWriter(file)
  pw_build.write(buildPropertiesContent)
  pw_build.close
  file = new File(rootDir + projectRootDir + projectDir + pluginsSbt)
  val pw_plugins = new PrintWriter(file)
  pw_plugins.write(pluginsSbtContent)
  pw_plugins.close
  file = new File(rootDir + projectRootDir + srcDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + srcDir + mainDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + srcDir + mainDir + resourceDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + srcDir + mainDir + resourceDir + flowJson)
  val pw_flow = new PrintWriter(file)
  pw_flow.write(graphConfigStr)
  pw_flow.close
  file = new File(rootDir + projectRootDir + srcDir + mainDir + scalaDir)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + srcDir + mainDir + scalaDir + jarDirName)
  if (!file.exists()) file.mkdir()
  file = new File(rootDir + projectRootDir + srcDir + mainDir + scalaDir + jarDirName + configScala)
  val pw_config = new PrintWriter(file)
  pw_config.write(configScalaContent)
  pw_config.close
  file = new File(rootDir + projectRootDir + srcDir + mainDir + scalaDir + jarDirName + templateGraphJarScala)
  val pw_templateGraphJar = new PrintWriter(file)
  pw_templateGraphJar.write(templateGraphJarScalaContent)
  pw_templateGraphJar.close

  file = new File(rootDir + projectRootDir + projectRootDir + ".tar.gz")

  val fileList: List[File] = List(
    new File(rootDir + projectRootDir),
    new File(rootDir + projectRootDir + projectDir + buildProperties),
    new File(rootDir + projectRootDir + projectDir + pluginsSbt),
    new File(rootDir + projectRootDir + srcDir + mainDir + resourceDir + flowJson),
    new File(rootDir + projectRootDir + srcDir + mainDir + scalaDir + jarDirName + configScala),
    new File(rootDir + projectRootDir + srcDir + mainDir + scalaDir + jarDirName + templateGraphJarScala)
  )

  import org.apache.commons.compress.archivers.tar.{TarArchiveOutputStream, TarArchiveEntry}
  val fos: FileOutputStream = new FileOutputStream(rootDir + projectRootDir + ".tar.gz")
  val bos: BufferedOutputStream = new BufferedOutputStream(fos)
  val gos: GZIPOutputStream = new GZIPOutputStream(bos)
  val taos: TarArchiveOutputStream = new TarArchiveOutputStream(gos)
  taos.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_STAR)
  taos.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU)
  fileList.foreach(file => {
    addFileToCompression(taos, file, ".")
  })
  taos.close()
  fos.close()

  def addFileToCompression(taos: TarArchiveOutputStream, file: File, dir: String) {
    val tae: TarArchiveEntry = new TarArchiveEntry(file, dir)
    taos.putArchiveEntry(tae)
    if(file.isDirectory()){
      taos.closeArchiveEntry()
      file.listFiles().foreach(childFile =>
        addFileToCompression(taos, childFile, dir + "/" + childFile.getName())
      )
    } else{
      val fis: FileInputStream = new FileInputStream(file)
      IOUtils.copy(fis, taos)
      taos.flush()
      taos.closeArchiveEntry()
    }
  }

  //val allProjectContent = Source.fromInputStream(classLoader.getResourceAsStream(rootDir + projectRootDir + "/_")).mkString
//  val pw_project = new PrintWriter(file)
//  pw_project.write(templateGraphJarScalaContent)
//  pw_project.close
//  //     aegis-flow-ying/sfdasdfafas   /tmp
  //  aegis-flow-ying.tar.gz           /tmp  aegis-flow-ying.tar.gz
  //

}
