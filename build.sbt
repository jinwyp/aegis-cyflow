import com.trueaccord.scalapb.{ScalaPbPlugin => PB}
import spray.revolver.RevolverPlugin._


enablePlugins(JavaServerAppPackaging)

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

    "com.typesafe.akka" %% "akka-cluster" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaV,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.scala-lang" % "scala-compiler" % scalaVersion.value,

    "com.typesafe.akka" %% "akka-persistence" % akkaV,
    "org.iq80.leveldb"            % "leveldb"          % "0.7",
    "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8",

    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,

    "com.typesafe.slick" %% "slick" % slickVersion,
    "org.flywaydb" % "flyway-core" % "3.2.1",

    "com.zaxxer" % "HikariCP" % "2.4.5",
    "org.slf4j" % "slf4j-nop" % "1.6.4",

    "mysql" % "mysql-connector-java" % "6.0.5",

    "org.scalatest" %% "scalatest" % scalaTestV % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV % "test",
    "com.wix" %% "accord-core" % "0.6",
    "com.softwaremill.quicklens" %% "quicklens" % "1.4.8",
    "com.typesafe.akka" % "akka-http-spray-json-experimental_2.11" % "2.4.11",

    "com.typesafe.akka" %% "akka-camel"   % "2.4.11",
    "org.apache.camel"  %  "camel-jetty"  % "2.16.4",
    "org.apache.camel"  %  "camel-quartz" % "2.16.4",

    "com.github.swagger-akka-http" %% "swagger-akka-http" % "0.7.2",
    "ch.qos.logback" % "logback-classic" % "1.1.3",
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,

    // lens
    "com.softwaremill.quicklens" % "quicklens_2.11" % "1.4.8",

    // neo4j-scala
    "eu.fakod"  %% "neo4j-scala" % "0.3.3",

    // scala-pb
    "com.trueaccord.scalapb"  %% "scalapb-runtime"  % "0.5.34"  % PB.protobufConfig,

    "org.scalaz" %% "scalaz-core" % "7.2.8",

    //session
    "com.softwaremill.akka-http-session" %% "core" % "0.3.0"
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



