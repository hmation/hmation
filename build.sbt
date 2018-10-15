import Versions._

scalaVersion := "2.12.7"
name := """hMation"""

libraryDependencies ++= {

  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-persistence" % akkaV,

    // Akka Http
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,

    // Loging
    "ch.qos.logback" % "logback-classic" % logbackV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,

    // Testing
    "org.scalatest" %% "scalatest" % scalatestV % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  )
}

mainClass in Compile := Some("com.hmation.core.hMationBootstrap")

resolvers += "Typesafe repository plugin" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"
