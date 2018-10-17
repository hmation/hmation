package com.hmation.core.device

import java.util.concurrent.TimeUnit.SECONDS

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.persistence.{PersistentActor, SnapshotOffer}
import akka.util.Timeout
import com.hmation.core.ConnectorRegistry.Lookup
import com.hmation.core.device.ShutterAggregate._

import scala.concurrent.Await

object ShutterAggregate {

  def props(id: String, connectorRegistry: ActorRef) = Props(classOf[ShutterAggregate], id, connectorRegistry)

  // commands
  trait ShutterCommand
  case class MoveShutter(position: Int) extends ShutterCommand
  object CloseShutter extends MoveShutter(100)
  object OpenShutter extends MoveShutter(0)
  case object PrintStatus extends ShutterCommand
  case object GetStatus extends ShutterCommand

  // events
  class ShutterEvent(val newPosition: Int)
  case object ShutterClosed extends ShutterEvent(100)
  case object ShutterOpened extends ShutterEvent(0)
  case class ShutterMoved(position: Int) extends ShutterEvent(position)

  // state
  case class ShutterState(
    connectorType: String,
    position: Int) {

    if (position < 0 || position > 100) throw new IllegalArgumentException("Position has to be in <0,100> range.")

    def isClosed = position == 100
    def isOpened = !isClosed
    def isFullyOpened = position == 0
  }

  private val SnapshotInterval = 1000
}

class ShutterAggregate(id: String, connectorRegistry: ActorRef) extends PersistentActor with ActorLogging {

  override def persistenceId = id

  var shutterState: ShutterState = ShutterState("blebox-shutter-connector", 0)
  var shutterConnector: ActorRef = _

  override def preStart() = {
    log.info(s"Created: $persistenceId")
    log.info(s"Actor Selection: ${context.self.path}")
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
        if (lastSequenceNr % SnapshotInterval == 0 && lastSequenceNr != 0) saveSnapshot(shutterState)
      }
    case CloseShutter =>
      persist(ShutterClosed) { event =>
        updateShutterState(event)
        shutterConnector ! CloseShutter
        context.system.eventStream.publish(event)
        if (lastSequenceNr % SnapshotInterval == 0 && lastSequenceNr != 0) saveSnapshot(shutterState)
      }
    case OpenShutter =>
      persist(ShutterOpened) { event =>
        updateShutterState(event)
        shutterConnector ! OpenShutter
        context.system.eventStream.publish(event)
        if (lastSequenceNr % SnapshotInterval == 0 && lastSequenceNr != 0) saveSnapshot(shutterState)
      }
    case PrintStatus => log.info(s"state: $shutterState")
    case GetStatus => sender() ! (persistenceId, shutterState.connectorType, shutterState.position)
  }

  def updateShutterState(event: ShutterEvent): Unit =
    shutterState = ShutterState(shutterState.connectorType, event.newPosition)
}
