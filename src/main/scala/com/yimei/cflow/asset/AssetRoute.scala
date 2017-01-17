package com.yimei.cflow.asset

import java.io.FileOutputStream
import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.Multipart
import akka.http.scaladsl.model.Multipart.FormData.BodyPart
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.directives.BasicDirectives.{extractSettings => _, pass => _}
import akka.http.scaladsl.server.directives.CacheConditionDirectives.{conditional => _}
import akka.http.scaladsl.server.directives.RouteDirectives.{complete => _, reject => _}
import akka.util.ByteString
import com.yimei.cflow.api.models.database.AssetDBModel.AssetEntity
import com.yimei.cflow.api.util.DBUtils.dbrun
import com.yimei.cflow.asset.db.AssetTable
import com.yimei.cflow.config.CoreConfig._
import com.yimei.cflow.config.DatabaseConfig._
import com.yimei.cflow.exception.DatabaseException
import com.yimei.cflow.graph.cang.exception.BusinessException
import com.yimei.cflow.graph.cang.models.CangFlowModel.FileObj

import scala.concurrent.Future

class AssetRoute extends AssetTable with SprayJsonSupport {

  import driver.api._

  val fileRootPath: String = coreConfig.getString("file.root")

  import java.io.File

  import scala.concurrent.duration._

  /**
    * GET asset/:asset_id  -- 下载asset_id资源
    */
  def downloadFile: Route = get {
    path("file" / Segment) { id =>
      val file: Future[AssetEntity] = dbrun(assetClass.filter(f => f.asset_id === id).result.head) recover {
        case _ => throw new DatabaseException("该文件不存在")
      }
      onComplete(file) { f =>
        val url = f.map(f => f.url).get
        val newFile = new File(fileRootPath + url)
        println(newFile.getName)
        println(newFile.getName)
        println(newFile.getName)
        println(newFile.getName)
        getFromFile(newFile)
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
    * 2. 组织1, 2, 3, 4,5信息, 保存到数据库记录
    *
    * @return
    */

  def uploadFile: Route = post {
    path("file") {
      entity(as[Multipart.FormData]) { fileData =>
        // 多个文件
        val result: Future[Map[String, String]] = fileData.parts.mapAsync[(String, String)](1) {
          case file:
            BodyPart if file.name == "file" =>
            val fileName = file.getFilename().get()
            val path = processFile(fileName, fileData)
            Future("path" -> path)
          case data: BodyPart => data.toStrict(2.seconds)
            .map(strict => data.name -> strict.entity.data.utf8String)
        }.runFold(Map.empty[String, String])((map, tuple) => map + tuple)
        onComplete(result) { data =>
          complete(insert(data.get))
        }
      }
    }
  }

  def insert(data: Map[String, String]): Future[FileObj] = {
    val path = data.get("path").get
    val party = data.get("role")
    val busi_type = data.getOrElse("busi_type", "default")
    val description: String = data.getOrElse("description", "")
    val uuId = path.substring(0, 36)
    val originName = path.substring(36, path.length)
    val url = uuId.replace("-", "/") + "/" + originName
    val suffix = originName.substring(originName.lastIndexOf('.') + 1)
    val fileType = getFileType(suffix)
    val assetEntity: AssetEntity = new AssetEntity(None, uuId, fileType, busi_type, "username", party, Some(description), url, originName, None)
    val temp: Future[Int] = dbrun(assetClass.insertOrUpdate(assetEntity)) recover {
      case _ => throw BusinessException(s"$url 上传失败")
    }

    temp.map(f => FileObj(uuId, originName, fileType, busi_type, party))
  }

  def getFileType(suffix: String): Int = {
    val imageArray = Array("jpg", "jpeg", "gif", "png", "bmp")
    if (suffix.toLowerCase == "pdf") 1
    else if (imageArray.contains(suffix.toLowerCase)) 2
    else if (suffix.toLowerCase == "doc" || suffix.toLowerCase == "docx") 3
    else if (suffix.toLowerCase == "xls" || suffix.toLowerCase == "xlsx") 4
    else 0
  }

  private def processFile(fileOriginName: String, fileData: Multipart.FormData): String = {
    val uuId = UUID.randomUUID().toString
    val dirPath = uuId.replace("-", "/")
    val newDir = new File(fileRootPath + dirPath)
    newDir.mkdirs()
    val filePath = fileRootPath + dirPath + "/" + fileOriginName
    val fileOutput = new FileOutputStream(filePath)
    val res = fileData.parts.mapAsync(1) {
      bodyPart =>
        def writeFileOnLocal(array: Array[Byte], byteString: ByteString): Array[Byte] = {
          val byteArray: Array[Byte] = byteString.toArray
          fileOutput.write(byteArray)
          array ++ byteArray
        }
        bodyPart.entity.dataBytes.runFold(Array[Byte]())(writeFileOnLocal)
    }.runFold(0)(_ + _.length) + "Hello"
    val path = uuId + fileOriginName + "." + res
    path.substring(0, path.lastIndexOf("."))
  }


  def route: Route = downloadFile ~ uploadFile
}

object AssetRoute {

  def route: Route = AssetRoute().route

  def apply(): AssetRoute = new AssetRoute()
}
