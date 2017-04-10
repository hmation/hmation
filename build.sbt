scalaVersion := "2.12.1"
name := """hMation"""

libraryDependencies ++= {
  val akkaVersion = "2.5.0-RC2"
  val akkaHttpVersion = "10.0.5"
  val logbackVersion = "1.2.3"
  val scalatestVersion = "3.0.1"
  val mockitoVersion = "2.7.22"

  Seq(
    // Akka
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    // Akka Goodies
    "ch.qos.logback" % "logback-classic" % logbackVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

    // Akka Http
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,

    // Testing
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test",
    "org.scalatest" %% "scalatest" % scalatestVersion % "test",
    "org.mockito" % "mockito-core" % mockitoVersion % "test"
  )
}

mainClass in Compile := Some("com.hmation.core.hMationBootstrap")

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"