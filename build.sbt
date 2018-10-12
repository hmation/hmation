scalaVersion := "2.12.7"
name := """hMation"""

libraryDependencies ++= {
  val akkaVersion = "2.5.17"
  val logbackVersion = "1.2.3"
  val scalatestVersion = "3.0.1"

  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-persistence" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion,

    // Akka Goodies
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

    // Testing
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )
}

mainClass in Compile := Some("com.hmation.core.hMationBootstrap")

resolvers += "Typesafe repository plugin" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"
