package com.yimei.cflow.config

import java.io.{ByteArrayOutputStream, File, OutputStreamWriter, Writer}

import freemarker.template.{Configuration, TemplateExceptionHandler}

/**
  * Created by hary on 17/1/3.
  */
object FreemarkerConfig extends CoreConfig {

  // Create your Configuration instance, and specify if up to what FreeMarker
  // version (here 2.3.25) do you want to apply the fixes that are not 100%
  // backward-compatible. See the Configuration JavaDoc for details.
  val ftl = new Configuration(Configuration.VERSION_2_3_23);

  // Specify the source where the template files come from. Here I set a
  // plain directory for it, but non-file-system sources are possible too:
  ftl.setDirectoryForTemplateLoading(new File("src-cang/ftl"));

  // Set the preferred charset template files are stored in. UTF-8 is
  // a good choice in most applications:
  ftl.setDefaultEncoding("UTF-8");

  // Sets how errors will appear.
  // During web page *development* TemplateExceptionHandler.HTML_DEBUG_HANDLER is better.
  ftl.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

  // Don't log exceptions inside FreeMarker that it will thrown at you anyway:
  ftl.setLogTemplateExceptions(false);


  def apply(template: String) = ftl.getTemplate(template);

  val staticPath = "asdfasdf/asfasdf"

  def render(template: String, data: Option[Map[String, String]] = None ) = {
    val os = new ByteArrayOutputStream()
    val out = new OutputStreamWriter(os)
    ftl.getTemplate(template).process(data, out)
    os.toString
  }

}
