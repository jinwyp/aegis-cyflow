package com.yimei.cflow.config

import java.io.{ByteArrayOutputStream, File, OutputStreamWriter, Writer}

import freemarker.template.{Configuration, TemplateExceptionHandler}

/**
  * Created by hary on 17/1/3.
  */
object FreemarkerConfig extends CoreConfig {

  val ftl = new Configuration(Configuration.VERSION_2_3_23);
  ftl.setDirectoryForTemplateLoading(new File("src-cang/ftl"));
  ftl.setDefaultEncoding("UTF-8");
  ftl.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
  ftl.setLogTemplateExceptions(false);

  val staticPath = coreConfig.getString("cang.ftl")

  def render(template: String, data: Option[Map[String, String]] = None ) = {
    val os = new ByteArrayOutputStream()
    val out = new OutputStreamWriter(os)
    ftl.getTemplate(template).process(data, out)
    os.toString
  }

}
