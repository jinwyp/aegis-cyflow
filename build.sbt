import com.trueaccord.scalapb.{ScalaPbPlugin => PB}
import spray.revolver.RevolverPlugin._


enablePlugins(JavaServerAppPackaging)

name := "aegis-cflow"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += Resolver.jcenterRepo

resolvers += "OSChina Maven Repository" at "http://maven.oschina.net/content/groups/public/"

externalResolvers := Resolver.withDefaultResolvers(resolvers.value, mavenCentral = false)

libraryDependencies ++= {
  val akkaV = "2.4.16"
  val scalaTestV = "3.0.0"
  val slickVersion = "3.1.1"
  val akkaHttp = "10.0.1"
  val circeV = "0.5.1"
  Seq(

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
    "com.typesafe.akka" %% "akka-http-core" % akkaHttp,
    "com.typesafe.akka" %% "akka-http" % akkaHttp,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttp % "test",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttp,
    "com.typesafe.akka" %% "akka-http-jackson" % akkaHttp,
    "com.typesafe.akka" %% "akka-http-xml" % akkaHttp,
  //  "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.7.2",
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
    "com.typesafe.akka" %% "akka-camel"   % akkaV,
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

PB.protobufSettings
PB.runProtoc in PB.protobufConfig := {
  args => com.github.os72.protocjar.Protoc.runProtoc("-v300" +: args.toArray)
}
version in PB.protobufConfig := "3.0.0-beta-3"

mainClass in assembly := Some("com.yimei.cflow.ServiceTest") //optional

import Resolvers._

mainClass in reStart := Some("com.yimei.cflow.ServiceTest")

assemblyMergeStrategy in assembly := {
//  case e   =>
//    println(s"!!!!!!!!!!!!!$e")
//    MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "MANIFEST.MF" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "DEPENDENCIES" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "INDEX.LIST" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "LICENSES.txt" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "pom.properties" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "reference.conf" => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last endsWith "pom.xml" => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last endsWith "component.properties" => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last endsWith "com.fasterxml.jackson.databind.Module" => MergeStrategy.concat
  case PathList(ps @ _*) if ps.last endsWith "org.neo4j.kernel.Version" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "org.neo4j.kernel.extension.KernelExtensionFactory" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "TypeConverter" => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith "StaticLoggerBinder.class" => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "StaticMDCBinder.class" => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "StaticMarkerBinder.class" => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "rootdoc.txt" => MergeStrategy.rename
  case PathList(ps @ _*) if Assembly.isReadme(ps.last) || Assembly.isLicenseFile(ps.last) =>  MergeStrategy.rename
  case _ => MergeStrategy.deduplicate
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

lazy val publishSettings = Seq(
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { (repo: MavenRepository) => false},
  pomExtra := pomXml) ++ xerial.sbt.Sonatype.sonatypeSettings

lazy val pomXml = {
  <url>https://github.com/epiphyllum/zflow</url>
    <licenses>
      <license>
        <name>Apache License 2.0</name>
        <url>http://www.apache.org/licenses/</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:epiphyllum/zflow.git</url>
      <connection>scm:git:git@github.com:epiphyllum/zflow.git</connection>
    </scm>
    <developers>
      <developer>
        <id>hary</id>
        <name>hary</name>
        <url>http://github.com/epiphyllum</url>
      </developer>
    </developers>
}

lazy val releaseSettings = sbtrelease.ReleasePlugin.releaseSettings ++ Seq(
  sbtrelease.ReleasePlugin.ReleaseKeys.publishArtifactsAction := PgpKeys.publishSigned.value
)



