name := "aegis-cflow"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaV = "2.4.11"
  val scalaTestV = "3.0.0"
  val slickVersion = "3.1.1"
  val circeV = "0.5.1"
  Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "de.heikoseeberger" %% "akka-http-circe" % "1.6.0",

    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
    "org.flywaydb" % "flyway-core" % "3.2.1",

    "com.zaxxer" % "HikariCP" % "2.4.5",
    "org.slf4j" % "slf4j-nop" % "1.6.4",

    "mysql" % "mysql-connector-java" % "6.0.5",

    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
    "ru.yandex.qatools.embed" % "postgresql-embedded" % "1.15" % "test",
    "com.wix" %% "accord-core" % "0.6",
    "com.softwaremill.quicklens" %% "quicklens" % "1.4.8",
    "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11",

    "com.typesafe.akka" %% "akka-camel"   % "2.4.11",
    "org.apache.camel"  %  "camel-jetty"  % "2.16.4",
    "org.apache.camel"  %  "camel-quartz" % "2.16.4",

    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.7.2",
    "ch.qos.logback" % "logback-classic" % "1.1.3"
  )
}

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xlint",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-feature",
  "-language:_"
)