package app.hmation.extension.api

import akka.actor.Actor

object ShutterExtensionApi {

  object commands {

    trait ShutterExtensionCommand
    case class MoveShutter(position: Int) extends ShutterExtensionCommand
    object CloseShutter extends MoveShutter(100)
    object OpenShutter extends MoveShutter(0)

  }

  object events {

    trait ShutterExtensionEvent

  }

  trait ShutterExtension extends Actor

}
