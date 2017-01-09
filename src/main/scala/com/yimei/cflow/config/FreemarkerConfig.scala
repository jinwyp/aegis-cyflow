package com.yimei.cflow.config

import java.io.{ByteArrayOutputStream, File, OutputStreamWriter, Writer}
import java.util

import freemarker.template.{Configuration, TemplateExceptionHandler}
import java.util.{Map => JMap}

import akka.http.scaladsl.model.ContentTypes.`text/html(UTF-8)`
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.util.ByteString
import com.yimei.cflow.config.CoreConfig._
import akka.stream.scaladsl.Source

import scala.concurrent.Future

/**
  * Created by hary on 17/1/3.
  */
object FreemarkerConfig {

  val ftlConfig = new Configuration(Configuration.VERSION_2_3_23);
  ftlConfig.setDirectoryForTemplateLoading(new File("src-cang/ftl"));
  ftlConfig.setDefaultEncoding("UTF-8");
  ftlConfig.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  ftlConfig.setLogTemplateExceptions(false);

  val staticPathAdmin = "/static/admin"
  val staticPath = "src-cang/frontend/src"
   // val staticEnv = coreConfig.getAnyRef("cang.env")
  val staticEnv = "fortest"



  // ftl   ==  ftl("admin/login.ftl", Some())


  //val comp = complete()

  def ftl(template:String,tdata:JMap[String, String] = new java.util.HashMap[String, String]()) ={

    val src =Source.fromFuture(Future {
      val os = new ByteArrayOutputStream()
      val out = new OutputStreamWriter(os)
      tdata.put("staticPathAdmin", staticPathAdmin)
      tdata.put("env", "staging")
      ftlConfig.getTemplate(template).process(tdata, out)
      ByteString(os.toString)
    })
    complete(HttpResponse(status = StatusCodes.OK, entity = HttpEntity(`text/html(UTF-8)`, src)))
  }


  def render(template: String, data: Option[JMap[String, String]] = None ) = {
    val os = new ByteArrayOutputStream()
    val out = new OutputStreamWriter(os)

    val tdata = new java.util.HashMap[String, String]()
    tdata.putAll(data.getOrElse(new util.HashMap[String, String]()))
    tdata.put("staticPathAdmin", "/src-cang/frontend/src")
    tdata.put("env", "staging")

    ftlConfig.getTemplate(template).process(tdata, out)
    os.toString
  }

}
