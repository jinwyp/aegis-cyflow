package com.yimei.cflow.swagger

import scala.reflect.runtime.{universe => ru}
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.github.swagger.akka._
import com.github.swagger.akka.model.`package`.Info
import com.yimei.cflow.config.Core
import io.swagger.models.{ExternalDocs, Tag}
import io.swagger.models.auth.BasicAuthDefinition

class SwaggerDocService(system: ActorSystem) extends SwaggerHttpService with HasActorSystem with Core {
  override implicit val actorSystem: ActorSystem = system
  override implicit val materializer: ActorMaterializer = ActorMaterializer()
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
  override val host = config.getString("swagger.url")
  override val info = Info(version = "1.0")
  override val externalDocs = Some(new ExternalDocs("Core Docs", "http://acme.com/docs"))
  override val securitySchemeDefinitions = Map("basicAuth" -> new BasicAuthDefinition())

  override def swaggerConfig = {
    val swagger = super.swaggerConfig

    val tags =  new java.util.ArrayList[Tag]()

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


// http://localhost:9000/swagger/index.html#!/createAccount/创建账户
// http://localhost:9000/swagger/index.html#!/%E8%BD%AC%E6%AC%BE%E7%9B%B8%E5%85%B3/资金账户之间转款查询