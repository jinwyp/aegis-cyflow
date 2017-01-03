package com.yimei.cflow.config

import java.io.{ByteArrayOutputStream, File, OutputStreamWriter, Writer}
import java.util

import freemarker.template.{Configuration, TemplateExceptionHandler}
import java.util.{Map => JMap}

/**
  * Created by hary on 17/1/3.
  */
object FreemarkerConfig extends CoreConfig {

  val ftl = new Configuration(Configuration.VERSION_2_3_23);
  ftl.setDirectoryForTemplateLoading(new File("src-cang/ftl"));
  ftl.setDefaultEncoding("UTF-8");
  ftl.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  ftl.setLogTemplateExceptions(false);

  val staticPathAdmin = coreConfig.getString("cang.ftl")

  def render(template: String, data: Option[JMap[String, String]] = None ) = {
    val os = new ByteArrayOutputStream()
    val out = new OutputStreamWriter(os)

    val tdata = new java.util.HashMap[String, String]()
    tdata.putAll(data.getOrElse(new util.HashMap[String, String]()))
    tdata.put("staticPathAdmin", staticPathAdmin)
    tdata.put("env", "staging")

    ftl.getTemplate(template).process(tdata, out)
    os.toString
  }

}
