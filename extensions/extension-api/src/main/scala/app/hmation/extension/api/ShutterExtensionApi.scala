package app.hmation.extension.api

trait ShutterExtensionApi {

  def moveShutter(position: Int)

  def closeShutter

  def openShutter

}
