import Dependencies._

ThisBuild / scalaVersion     := "2.13.4"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val AkkaVersion = "2.6.12"

lazy val root = (project in file("."))
  .enablePlugins(JavaAppPackaging)
  .enablePlugins(NativeImagePlugin)
  .settings(
    name := "graaltest2",
    libraryDependencies ++= List(
      "ch.qos.logback" % "logback-classic" % "1.2.3",
      "ch.qos.logback" % "logback-core" % "1.2.3",
      "com.softwaremill.sttp.client3" %% "core" % "3.1.0",
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.1",
      "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
      scalaTest % Test),
    Compile / mainClass := Some("example.Hello"),
    nativeImageVersion := "20.3.1",
    nativeImageAgentOutputDir := baseDirectory.value / "native-image-configs",
    nativeImageOptions ++= List(
      "--static",
      "--report-unsupported-elements-at-runtime",
      "--no-fallback",
      "--allow-incomplete-classpath",
      "-H:EnableURLProtocols=https",
      "-H:IncludeResources=foo.txt",
      "-H:IncludeResources=logback.xml",
      s"-H:ResourceConfigurationFiles=${nativeImageAgentOutputDir.value / "resource-config.json"}",
      s"-H:ReflectionConfigurationFiles=${nativeImageAgentOutputDir.value / "reflect-config.json"}",
    )
  )
