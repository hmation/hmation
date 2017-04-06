scalaVersion := "2.12.1"
name := """hMation"""


libraryDependencies ++= {
  val akkaVersion = "2.5.0-RC2"
  val akkaHttpVersion = "10.0.5"
  val logbackVersion = "1.2.3"

  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    // Akka Goodies
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

  // Akka Http
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
  )
}


mainClass in Compile := Some("com.hmation.core.hMationBootstrap")