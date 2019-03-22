import versions._

dependsOn(
  modules.extensions.`extension-api`
)

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
    "org.slf4j" % "slf4j-api" % slf4jV,

    // Goodies
    "com.softwaremill.common" %% "id-generator" % idGeneratorV,
    "org.fusesource.leveldbjni" % "leveldbjni-all" % leveldbV,
    "org.iq80.leveldb" % "leveldb" % "0.7",
    "com.typesafe" % "config" % configV,

    // Testing
    "org.scalatest" %% "scalatest" % scalatestV % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test"
  )
}

mainClass in Compile := Some("app.hmation.core.hMationBootstrap")