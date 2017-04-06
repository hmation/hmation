package com.hmation.core.device

import akka.actor.{Actor, ActorLogging, Props}
import com.hmation.core.device.Switch.SwitchStatus.SwitchStatus
import com.hmation.core.device.Switch.{GetStatus, SwitchStatus, TurnOff, TurnOn}

object Switch {

  def props(status: SwitchStatus) = Props(classOf[Switch], status)

  case object GetStatus

  object TurnOn
  object TurnOff

  object SwitchStatus extends Enumeration {
    type SwitchStatus = Value
    val ON, OFF = Value
  }
}

class Switch(var status: SwitchStatus) extends Actor with ActorLogging {
  override def receive: Receive = {
    case GetStatus => {
      log.info(s"Status: $status")
      sender() ! status
    }
    case TurnOn => {
      log.info(s"Switching from: $status to ${SwitchStatus.ON}")
      status = SwitchStatus.ON
    }
    case TurnOff => {
      log.info(s"Switching from: $status to ${SwitchStatus.OFF}")
      status = SwitchStatus.OFF
    }
  }
}
