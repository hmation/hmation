scalaVersion := "2.12.1"
name := """hMation"""

libraryDependencies ++= {
  val akkaVersion = "2.5.0"
  val akkaHttpVersion = "10.0.5"
  val logbackVersion = "1.2.3"
  val scalatestVersion = "3.0.1"
  val mockitoVersion = "2.7.22"
  val playVersion = "2.6.0-M5"
  val guiceVersion = "4.1.0"

  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "org.scalatest" %% "scalatest" % scalatestVersion,

    // Akka Goodies
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

    // Akka Http
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

    // Play
    "com.typesafe.play" %% "play" % playVersion,
    "com.google.inject" % "guice" % guiceVersion,

    // Testing
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  )
}

mainClass in Compile := Some("com.hmation.core.hMationBootstrap")

enablePlugins(PlayScala)
disablePlugins(PlayLayoutPlugin)
PlayKeys.playMonitoredFiles ++= (sourceDirectories in (Compile, TwirlKeys.compileTemplates)).value

resolvers += "Typesafe repository plugin" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"
