package com.hmation.core.device

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import com.hmation.core.device.Shutter._

object Shutter {

  def props() = Props(classOf[Shutter])

  // commands
  trait ShutterCommand
  case class MoveShutter(position: Int) extends ShutterCommand
  object CloseShutter extends MoveShutter(100)
  object OpenShutter extends MoveShutter(0)
  case object PrintStatus extends ShutterCommand

  // events
  class ShutterEvent(val newPosition: Int)
  case object ShutterClosed extends ShutterEvent(100)
  case object ShutterOpened extends ShutterEvent(0)
  case class ShutterMoved(position: Int) extends ShutterEvent(position)

  case class ShutterState(position: Int) {

    if (position < 0 || position > 100) throw new IllegalArgumentException("Position has to be in <0,100> range.")

    def isClosed = position == 100
    def isOpened = !isClosed
    def isFullyOpened = position == 0
  }
}

class Shutter extends PersistentActor with ActorLogging {

  val snapShotInterval = 1000

  override def persistenceId: String = "shutter"

  var shutterState: ShutterState = new ShutterState(0)

  val receiveRecover: Receive = {
    case evt: ShutterEvent => updateShutterState(evt)
    case SnapshotOffer(_, snapshot: ShutterState) => shutterState = snapshot
  }
  override def receiveCommand: Receive = {
    case moveShutterCommand: MoveShutter =>
      persist(ShutterMoved(moveShutterCommand.position)) { event ⇒
        updateShutterState(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(shutterState)
      }
    case CloseShutter =>
      persist(ShutterClosed) { event ⇒
        updateShutterState(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(shutterState)
      }
    case OpenShutter =>
      persist(ShutterOpened) { event ⇒
        updateShutterState(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(shutterState)
      }
    case "print" => println(shutterState)
  }

  def updateShutterState(event: ShutterEvent): Unit =
    shutterState = ShutterState(event.newPosition)
}
