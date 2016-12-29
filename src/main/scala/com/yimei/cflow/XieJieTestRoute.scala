package com.yimei.cflow

import java.io.{FileNotFoundException, FileOutputStream, IOException}
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.scaladsl.FileIO
import akka.util.ByteString
import com.yimei.cflow.config.{ApplicationConfig, CoreConfig}

import scala.concurrent.Future

/**
  * Created by liuxinjie on 2016/12/23.
  */
class XieJieTestRoute extends App with CoreConfig with ApplicationConfig with SprayJsonSupport {

  val f = Future {
    "abc"
  }

  def hello: Route = get {
    path("hello") {
      complete("Hello")
    }
  }

  def hello1: Route = get {
    pathPrefix("hello" / Segment) { id =>
      pathEnd(
        complete("Hello" + " -------  |||| " + id)
      )
    }
  }

  def hello2: Route = get {
    pathPrefix("hello" / Segment / Segment) { (id, name) =>
      pathEnd {
        println(" --- " + id + " --- " + name)
        complete("Hello" + " - " + id + " : " + name)
      }
    }
  }

  def hello3: Route = get {
    pathPrefix("hello" / Segment / Segment / Segment) { (id, name, phone) =>
      pathEnd {
        println(id + " --- " + name + " --- " + phone)
        complete("Test3" + " - " + id + " : " + name + " : " + phone)
      }
    }
  }

  import com.yimei.cflow.graph.cang.models.UserModel._

  def addUser: Route = post {
    pathPrefix("usertest") {
      pathEnd {
        entity(as[AddUser]) { user =>
          println(" -------- user --------- " + user.toString)
          complete(user)
        }
      }
    }
  }

  import java.io.File

  import akka.http.scaladsl.model.Multipart

  import scala.concurrent.duration._

  val localPath = coreConfig.getString("filePath")


  def uploadFile: Route = post {
    pathPrefix("apz" / "upload" / "file") {
      pathEnd {
        entity(as[Multipart.FormData]) { fileData =>
          complete {
            var fileOriginName: String = ""
            val extractedData: Future[Map[String, Any]] = fileData.parts.mapAsync[(String, Any)](1) {
              case file:
                BodyPart if file.name == "file" => val tempFile = File.createTempFile("process", "file")
                println(" ------------ file --------------- " + file.getFilename().get())
                fileOriginName = file.getFilename().get()
                val fileName = UUID.randomUUID().toString + fileOriginName
                val filePath = "./files/cang/" + fileName
                println(" 123 " + filePath)
                processFile(filePath, fileData)
                println(" ----------------- " + fileOriginName)
                file.entity.dataBytes.runWith(FileIO.toPath(tempFile.toPath)).map {
                  ioResult => s"file ${file.filename.fold("Unknown")(identity)}" -> s"${ioResult.count} bytes"
                }
              case data: BodyPart => data.toStrict(2.seconds)
                .map(strict => data.name -> strict.entity.data.utf8String)
            }.runFold(Map.empty[String, Any])((map, tuple) => map + tuple)

            println(" 133" + extractedData)
            extractedData.map {
              data => HttpResponse(StatusCodes.OK, entity = s"Data : ${data.mkString(", ")} has been successfully saved.")
            }.recover {
              case ex: Exception => HttpResponse(StatusCodes.InternalServerError, entity = s"Error in processing multipart form data due to ${ex.getMessage}")
            }
          }
        }
        //          println(" ----------- ")
        //          val fileName = UUID.randomUUID().toString
        //          val filePath = "./files/cang/" + fileName
        //          val fileOriginName: String = processFile(filePath, fileData)
        //          val fileObj = FileObj(fileName, fileOriginName, filePath)
        //          complete(fileObj)
      }
    }
  }

    private def processFile(filePath: String, fileData: Multipart.FormData) {
      val fileOutput = new FileOutputStream(filePath)
      fileData.parts.mapAsync(1) {
        bodyPart =>
          println(" 123456 ------ " + bodyPart.getName() + " --- " + bodyPart.getFilename())
          def writeFileOnLocal(array: Array[Byte], byteString: ByteString): Array[Byte] = {
            val byteArray: Array[Byte] = byteString.toArray
            fileOutput.write(byteArray)
            array ++ byteArray
          }
          bodyPart.entity.dataBytes.runFold(Array[Byte]())(writeFileOnLocal)
      }.runFold(0)(_ + _.length)
    }

  def route: Route = hello ~ hello1 ~ hello2 ~ hello3 ~ addUser ~ uploadFile


}

object XieJieTestRoute {

  def apply() = new XieJieTestRoute

  def route: Route = XieJieTestRoute().route
}


