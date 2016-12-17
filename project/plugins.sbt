logLevel := Level.Warn

// Informative Scala compiler errors
addSbtPlugin("com.softwaremill.clippy" % "plugin-sbt" % "0.3.0")

// Scala Protocol Buffers Compiler
addSbtPlugin("com.trueaccord.scalapb" % "sbt-scalapb" % "0.5.34")

addSbtPlugin("org.scoverage" %% "sbt-scoverage" % "1.5.0")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.4")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.1")

// Protoc-jar so we don't need the Protoc compiler
libraryDependencies += "com.github.os72" % "protoc-jar" % "3.0.0-b3"