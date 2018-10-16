package com.hmation.core.device

import akka.actor.{Actor, ActorLogging}

object Dimmer

class Dimmer extends Actor with ActorLogging {
  override def receive: Receive = {
    case _ => AnyRef
  }
}
