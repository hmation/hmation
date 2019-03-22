// core
lazy val `devices` = modules.core.`devices`

// extensions
lazy val `extensions-api` = modules.extensions.`extension-api`
lazy val `blebox` = modules.extensions.`blebox`

// root
lazy val `hmation` = (project in file("."))
  .aggregate(
    `devices`,
    `blebox`
  )

scalaVersion := "2.12.7"
name := "hmation"

resolvers += "Typesafe repository plugin" at "https://repo.scala-sbt.org/scalasbt/sbt-plugin-releases/"

cancelable in Global := true