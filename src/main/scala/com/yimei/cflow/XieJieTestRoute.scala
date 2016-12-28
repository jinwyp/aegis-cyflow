package com.yimei.cflow

import java.util.Optional

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.ResponseEntity
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.scaladsl.Source
import com.yimei.cflow.config.{ApplicationConfig, CoreConfig}

import scala.concurrent.Future
import scala.util.Random

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

  val numbers = Source.fromIterator(() =>
    Iterator.continually(Random.nextInt()))

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

  import java.io.FileOutputStream
  import java.util.UUID

  import akka.http.scaladsl.model.{HttpResponse, Multipart, StatusCodes}
  import akka.util.ByteString
  import com.yimei.cflow.graph.cang.models.CangFlowModel.FileObj

  val localPath = coreConfig.getString("filePath")


  def uploadFile: Route = post {
    pathPrefix("apz" / "upload" / "file") {
      pathEnd {
        entity(as[Multipart.FormData]) { (fileData: Multipart.FormData) =>
          println(" ================================= ")
          println(" ================================= ")
          println(" ----------------------------- ")
          val fileName = UUID.randomUUID().toString
          val filePath = "./files/cang/" + fileName + ".png"
          val fileOriginName: String = processFile(filePath, fileData)
          val fileObj = FileObj(fileOriginName, filePath)
          println(" eeeeeeeeeeeeeeeeeeeeeeee111111 ")
          println(fileObj.toString)
          complete(fileObj)
        }
      }
    }
  }

  private def processFile(filePath: String, fileData: Multipart.FormData): String = {
    val fileOutput = new FileOutputStream(filePath)
    var fileName: String = ""
    fileData.parts.mapAsync(1) {
      bodyPart =>
        println(" 123456 ------ " + bodyPart.getName() + " --- " + bodyPart.getFilename())
        def writeFileOnLocal(array: Array[Byte], byteString: ByteString): Array[Byte] = {
          val byteArray: Array[Byte] = byteString.toArray
          fileOutput.write(byteArray)
          array ++ byteArray
        }
        println(bodyPart.getName() == "file")
        if (bodyPart.getName().equals("file")) {
          fileName += String.valueOf(bodyPart.getFilename())
        }
        bodyPart.entity.dataBytes.runFold(Array[Byte]())(writeFileOnLocal)
    }.runFold(0)(_ + _.length)
    return fileName
  }

  def route: Route = hello ~ hello1 ~ hello2 ~ hello3 ~ addUser ~ uploadFile


}

object XieJieTestRoute {

  def apply() = new XieJieTestRoute

  def route: Route = XieJieTestRoute().route
}


