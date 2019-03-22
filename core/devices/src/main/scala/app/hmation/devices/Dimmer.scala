package app.hmation.devices

import akka.actor.{Actor, ActorLogging}

object Dimmer

class Dimmer extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ =>
      log.info("some")
  }
}
