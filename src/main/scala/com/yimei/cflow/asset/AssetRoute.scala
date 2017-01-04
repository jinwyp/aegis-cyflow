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

class AssetRoute extends ApplicationConfig with SprayJsonSupport {
  val rootPath = coreConfig.getString("filePath")
  val localPath = rootPath + "cang/"

  import scala.concurrent.duration._

  /**
    * GET asset/:asset_id  -- 下载asset_id资源
    *
    * @return
    */
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
    * POST asset/    -- 上传文件:
    *
    * 1. username        --> 上传用户
    * 2. gid             --> 用户组
    * 3. file_type       --> 后台计算   暂时依据后缀判断
    * 4. busi_type       --> 表单
    * 5. uri             --> 存储位置
    *
    * 算法:
    * 1. 保持文件到文件系统, 记录位置为uri   --->    uuid  $file_root/xxxx/xx/xxx/xxx.pdf
    *
    * 2. 组织1, 2, 3, 4,5信息, 保存到数据库记录
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

          val ff = result.map { data => {
            val url = data.get("url").get
            val originName = data.get("name").get
            FileObj(url.replace(localPath, ""), originName, url)
          }}

          complete(ff)

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

object AssetRoute {

  def route: Route = AssetRoute().route

  def apply(): AssetRoute = new AssetRoute()
}
