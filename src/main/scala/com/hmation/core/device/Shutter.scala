package com.hmation.core.device

import akka.actor.{Actor, ActorLogging, Props}
import com.hmation.core.device.Shutter.{GetStatus, MoveShutter, ShutterStatus}

object Shutter {

  def props(status: ShutterStatus) = Props(classOf[Shutter], status)

  case object GetStatus

  case class MoveShutter(position: Int)
  object Close extends MoveShutter(100)
  object Open extends MoveShutter(0)

  case class ShutterStatus(position: Int) {

    if(position < 0 || position > 100) throw new IllegalArgumentException("Position has to be in <0,100> range.")

    def isClosed = position == 100
    def isOpened = !isClosed
    def isFullyOpened = position == 0
  }
}

class Shutter(var shutterStatus: ShutterStatus) extends Actor with ActorLogging {
  override def receive: Receive = {
    case GetStatus => {
      log.info(s"In position: ${shutterStatus.position}")
      sender() ! ShutterStatus(shutterStatus.position)
    }
    case MoveShutter(desiredPosition) => {
      log.info(s"In position: ${shutterStatus.position}, moving to: $desiredPosition")
      shutterStatus = ShutterStatus(desiredPosition)
    }
  }
}
