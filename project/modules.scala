package modules

import sbt.{file, project}

object core extends moduleGroup {
  override def moduleGroupLocation: String = "core"

  lazy val `devices` = project in module("devices")
}


object extensions extends moduleGroup {
  override def moduleGroupLocation: String = "extensions"

  lazy val `extension-api` = project in module("extension-api")
  lazy val `blebox` = project in module("blebox")
}

trait moduleGroup {
  def moduleGroupLocation: String

  val module = (folder: String) => file(s"$moduleGroupLocation/$folder")
}
