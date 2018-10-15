package com.hmation.core.device

import java.util.concurrent.TimeUnit.SECONDS

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.persistence.{PersistentActor, SnapshotOffer}
import akka.util.Timeout
import com.hmation.core.ConnectorRegistry.Lookup
import com.hmation.core.device.Shutter._

import scala.concurrent.Await

object Shutter {

  def props(connectorRegistry: ActorRef) = Props(classOf[Shutter], connectorRegistry)

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

  case class ShutterState(
    connectorType: String,
    position: Int) {

    if (position < 0 || position > 100) throw new IllegalArgumentException("Position has to be in <0,100> range.")

    def isClosed = position == 100
    def isOpened = !isClosed
    def isFullyOpened = position == 0
  }

  private val SNAPSHOT_INTERVAL = 1000
}

class Shutter(connectorRegistry: ActorRef) extends PersistentActor with ActorLogging {

  override def persistenceId: String = "shutter"

  var shutterState: ShutterState = ShutterState("blebox-shutter-connector", 0)
  var shutterConnector: ActorRef = _

  override def preStart() = {
    implicit val timeout = Timeout(1, SECONDS)
    shutterConnector = Await.result(connectorRegistry ? Lookup("blebox-shutter-connector"), timeout.duration).asInstanceOf[ActorRef]
  }

  val receiveRecover: Receive = {
    case evt: ShutterEvent => updateShutterState(evt)
    case SnapshotOffer(_, snapshot: ShutterState) => shutterState = snapshot
  }

  override def receiveCommand: Receive = {
    case moveShutterCommand: MoveShutter =>
      persist(ShutterMoved(moveShutterCommand.position)) { event ⇒
        updateShutterState(event)
        shutterConnector ! moveShutterCommand
        context.system.eventStream.publish(event)
        if (lastSequenceNr % SNAPSHOT_INTERVAL == 0 && lastSequenceNr != 0) saveSnapshot(shutterState)
      }
    case CloseShutter =>
      persist(ShutterClosed) { event ⇒
        updateShutterState(event)
        shutterConnector ! CloseShutter
        context.system.eventStream.publish(event)
        if (lastSequenceNr % SNAPSHOT_INTERVAL == 0 && lastSequenceNr != 0) saveSnapshot(shutterState)
      }
    case OpenShutter =>
      persist(ShutterOpened) { event ⇒
        updateShutterState(event)
        shutterConnector ! OpenShutter
        context.system.eventStream.publish(event)
        if (lastSequenceNr % SNAPSHOT_INTERVAL == 0 && lastSequenceNr != 0) saveSnapshot(shutterState)
      }
    case "print" => log.info(s"state: $shutterState")
  }

  def updateShutterState(event: ShutterEvent): Unit =
    shutterState = ShutterState(shutterState.connectorType, event.newPosition)
}
