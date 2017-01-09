package com.yimei.cflow.swagger

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.swagger.akka._
import com.yimei.cflow.config.CoreConfig
import io.swagger.models.auth.BasicAuthDefinition
import com.yimei.cflow.config.CoreConfig._
import io.swagger.models.{ExternalDocs, Tag}
import com.github.swagger.akka.model.`package`.Info

import scala.reflect.runtime.{universe => ru}

class SwaggerDocService(system: ActorSystem) extends SwaggerHttpService with HasActorSystem  {

  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = coreMaterializer

  override val apiTypes = Seq(

    //    ru.typeOf[CreateAccount],
    //    ru.typeOf[CreateBankAccount],
    //    ru.typeOf[QB],
    //    ru.typeOf[QA],
    //    ru.typeOf[FZ],
    //    ru.typeOf[FQ],
    //    ru.typeOf[UF],
    //    ru.typeOf[UQ],
    //    ru.typeOf[TS],
    //    ru.typeOf[TSQ],
    //    ru.typeOf[TD],
    //    ru.typeOf[TDQ],
    //    ru.typeOf[QPF]

  )
  override val host = coreConfig.getString("swagger.url")
  override val info = Info(version = "1.0")
  override val externalDocs = Some(new ExternalDocs("Core Docs", "http://acme.com/docs"))
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())

  override def swaggerConfig = {
    val swagger = super.swaggerConfig

    val tags = new java.util.ArrayList[Tag]()

    // hack swagger for Tag support!!!!!
    // bugfix:  tagname must be english!!!!
    //    tags.add(new Tag().name("transfer").description("转款相关"))
    //    tags.add(new Tag().name("account").description("账户相关"))
    //    tags.add(new Tag().name("freeze").description("冻结相关"))
    //    tags.add(new Tag().name("printFlowQuery").description("打印明细"))

    swagger.tags(tags)
    swagger
  }

}

