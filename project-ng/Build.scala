import com.trueaccord.scalapb.ScalaPbPlugin
import sbt.Keys._
import sbt._
import spray.revolver.RevolverPlugin.autoImport.Revolver

object Resolvers {
}

object Depenencies {

  private val akkaV = "2.4.11"
  private val scalaTestV = "3.0.0"
  private val slickVersion = "3.1.1"
  private val circeV = "0.5.1"

  val appDependencies = Seq(
    // cluster
    "com.typesafe.akka" %% "akka-cluster" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaV,

    // compiler
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,

    // persistence
    "com.typesafe.akka" %% "akka-persistence" % akkaV,
    "com.hootsuite" %% "akka-persistence-redis" % "0.6.0",
    "org.iq80.leveldb"            % "leveldb"          % "0.7",
    "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",

    // akka-http
    "com.typesafe.akka" %% "akka-http-core" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
    "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11",
    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.7.2",
    "com.softwaremill.akka-http-session" %% "core" % "0.3.0",

    // database: slick and flyway
    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.flywaydb" % "flyway-core" % "3.2.1",
    "com.zaxxer" % "HikariCP" % "2.4.5",


    // mysql
    "mysql" % "mysql-connector-java" % "6.0.5",

    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "com.wix" %% "accord-core" % "0.6",
    "com.softwaremill.quicklens" %% "quicklens" % "1.4.8",

    // camel integration
    "com.typesafe.akka" %% "akka-camel"   % "2.4.11",
    "org.apache.camel"  %  "camel-jetty"  % "2.16.4",
    "org.apache.camel"  %  "camel-quartz" % "2.16.4",

    // logger
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,

    // lens
    "com.softwaremill.quicklens" % "quicklens_2.11" % "1.4.8",

    // neo4j-scala
    "eu.fakod"  %% "neo4j-scala" % "0.3.3",

    // scala-pb
    "com.trueaccord.scalapb"  %% "scalapb-runtime"  % "0.5.34"  % PB.protobufConfig,

    // scalaz
    "org.scalaz" %% "scalaz-core" % "7.2.8",

    //files tar.gz
    "org.apache.commons" % "commons-compress" % "1.12",

    //http cors
    //    "ch.megard" %% "akka-http-cors" % "0.1.10",

    "org.apache.commons" % "commons-compress" % "1.12",

    "org.freemarker" % "freemarker" % "2.3.23",

    "org.thymeleaf" % "thymeleaf" % "3.0.2.RELEASE"

  )
}

object BuildSettings {

  val buildOrganization = "com.yimei"
  val appName = "sbt-publish-example"
  val buildVersion = "0.0.1-SNAPSHOT"
  val buildScalaVersion = "2.11.8"
  val buildScalaOptions = Seq("-unchecked", "-deprecation", "-encoding", "utf8")

  import Dependencies._

  val buildSettings = Defaults.coreDefaultSettings ++ Seq(
    organization := buildOrganization,
    version := buildVersion,
    scalaVersion := buildScalaVersion,
    libraryDependencies ++= appDependencies,    // dependencies
    scalacOptions := buildScalaOptions
  ) ++ Revolver.settings
}

object PublishSettings {

  // publish settings
  val publishSettings = Seq(
    credentials += Credentials("Sonatype Nexus Repository Manager", "maven.yimei180.com", "admin", "admin123"),
    publishTo := Some("Sonatype Nexus Repository Manager" at "http://maven.yimei180.com/content/repositories/snapshots"),
    publishMavenStyle := true,
    isSnapshot := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { (repo: MavenRepository) => false },
    pomExtra := pomXml
  )

  lazy val pomXml = {
    <url>https://github.com/epiphyllum/sbt-publish-example</url>
      <licenses>
        <license>
          <name>Apache License 2.0</name>
          <url>http://www.apache.org/licenses/</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:epiphyllum/sbt-publish-example.git</url>
        <connection>scm:git:git@github.com:epiphyllum/sbt-publish-example.git</connection>
      </scm>
      <developers>
        <developer>
          <id>hary</id>
          <name>hary</name>
          <url>http://github.com/epiphyllum</url>
        </developer>
      </developers>
  }
}

object ApplicationBuild extends Build {

  import BuildSettings._
  import PublishSettings._

  lazy val root = Project(
    appName,
    file(".")).aggregate(engine, engineApi, organ, organApi)

  lazy val engineApi  = Project("engine-api",  file("engine-api"),  settings = buildSettings ++ publishSettings)
  lazy val egineUtil  = Project("engine-util", file("engine-util"), settings = buildSettings ++ publishSettings)
  lazy val engineCore = Project("engine-core", file("engine-core"), settings = buildSettings ++ publishSettings).dependsOn(engineCore, engineUtil)
}