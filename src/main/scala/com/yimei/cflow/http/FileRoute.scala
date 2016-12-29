package com.yimei.cflow.http

import java.io.{File, FileOutputStream}
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import com.yimei.cflow.config.ApplicationConfig
import com.yimei.cflow.graph.cang.models.CangFlowModel.FileObj

import scala.concurrent.{Future, future}

class FileRoute extends ApplicationConfig with SprayJsonSupport {

  val localPath = coreConfig.getString("filePath") + "cang/"

  import scala.concurrent.duration._

  def uploadFile: Route = post {
    pathPrefix("file" / "upload") {
      pathEnd {
        entity(as[Multipart.FormData]) { fileData =>
          val result: Future[Map[String, String]] = fileData.parts.mapAsync[(String, String)](1) {
            case file :
              BodyPart if file.name == "file" =>
              val fileName = UUID.randomUUID().toString + "-" + file.getFilename().get()
              val filePath = localPath + fileName
              processFile(filePath, fileData)
              Future ("url" -> filePath)
            case data: BodyPart => data.toStrict(2.seconds)
              .map(strict => data.name -> strict.entity.data.utf8String)
          }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)
          complete {
            result.map { data => {
              val url = data.get("url").get
              val originName = data.get("name").get
              FileObj(url.replace(localPath, ""), originName, url)
            }}
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

  def route: Route = uploadFile
}

object FileRoute {

  def apply(): FileRoute = new FileRoute()
  def route: Route = FileRoute().route
}
