package com.yimei.cflow.asset

import java.io.FileOutputStream
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.ByteString
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.graph.cang.models.CangFlowModel.FileObj

import scala.concurrent.Future

class FileRoute extends ApplicationConfig with SprayJsonSupport {
  val rootPath = coreConfig.getString("filePath")
  val localPath = rootPath + "cang/"

  import scala.concurrent.duration._

  def downloadFile: Route = get {
    pathPrefix("file" / "download") {
      pathEnd {
        parameter("url") { url =>
          if (url == null || !url.startsWith(rootPath)) {
            complete("error")
          }
          complete("ok")
        }
      }
    }
  }


  /**
    *
    * @return
    */
  def uploadFile: Route = post {
    pathPrefix("file" / "upload") {
      pathEnd {
        entity(as[Multipart.FormData]) { fileData =>
          // 多个文件
          val result: Future[Map[String, String]] = fileData.parts.mapAsync[(String, String)](1) {
            case file:
              BodyPart if file.name == "file" =>
              val fileName = UUID.randomUUID().toString + "-" + file.getFilename().get()
              val filePath = localPath + fileName
              processFile(filePath, fileData)
              Future("url" -> filePath)
            case data: BodyPart => data.toStrict(2.seconds)
              .map(strict => data.name -> strict.entity.data.utf8String)
          }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)


          complete {
            result.map { data => {
              val url = data.get("url").get
              val originName = data.get("name").get
              FileObj(url.replace(localPath, ""), originName, url)
            }
            }
          }
        }
      }
    }
  }

  private def processFile(filePath: String, fileData: Multipart.FormData) {
    val fileOutput = new FileOutputStream(filePath)
    fileData.parts.mapAsync(1) {
      bodyPart =>
        def writeFileOnLocal(array: Array[Byte], byteString: ByteString): Array[Byte] = {
          val byteArray: Array[Byte] = byteString.toArray
          fileOutput.write(byteArray)
          array ++ byteArray
        }
        bodyPart.entity.dataBytes.runFold(Array[Byte]())(writeFileOnLocal)
    }.runFold(0)(_ + _.length)
  }


  def route: Route = downloadFile ~ uploadFile
}

object FileRoute {

  def route: Route = FileRoute().route

  def apply(): FileRoute = new FileRoute()
}
