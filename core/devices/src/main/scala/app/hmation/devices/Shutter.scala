package app.hmation.devices

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.pattern.ask
import akka.persistence.{PersistentActor, SnapshotOffer}
import akka.util.Timeout
import app.hmation.core.ConnectorRegistry.Api.Lookup
import app.hmation.devices.Shutter.Commands._
import app.hmation.devices.Shutter.Config._
import app.hmation.devices.Shutter.Events._
import app.hmation.devices.Shutter.State._

import scala.concurrent.Await
import scala.concurrent.duration._

object Shutter {

  def props(id: String, connectorRegistry: ActorRef) = Props(classOf[Shutter], id, connectorRegistry)

  object Commands {
    trait ShutterCommand
    case class MoveShutter(position: Int) extends ShutterCommand
    object CloseShutter extends MoveShutter(100)
    object OpenShutter extends MoveShutter(0)
    case object PrintStatus extends ShutterCommand
    case object GetStatus extends ShutterCommand
  }

  object Events {
    class ShutterEvent(val newPosition: Int)
    case object ShutterClosed extends ShutterEvent(100)
    case object ShutterOpened extends ShutterEvent(0)
    case class ShutterMoved(position: Int) extends ShutterEvent(position)
  }

  object State {
    case class ShutterState(
      connectorType: String,
      position: Int) {

      if (position < 0 || position > 100) throw new IllegalArgumentException("Position has to be in <0,100> range.")

      def isClosed = position == 100
      def isOpened = !isClosed
      def isFullyOpened = position == 0
    }
  }

  object Config {
    val SnapshotInterval = 1000
  }
}

class Shutter(id: String, connectorRegistry: ActorRef) extends PersistentActor with ActorLogging {

  override def persistenceId = id

  var shutterState: ShutterState = ShutterState("blebox-shutter-connector", 0)
  var shutterConnector: ActorRef = _

  override def preStart() = {
    log.info(s"Created: $persistenceId")
    log.info(s"Actor Selection: ${context.self.path}")
    implicit val timeout = Timeout(1, SECONDS)
    // todo: make it not failing when the connector is not there
    shutterConnector = Await.result(connectorRegistry ? Lookup("blebox-shutter-connector"), timeout.duration).asInstanceOf[ActorRef]
  }

  val receiveRecover: Receive = {
    case evt: ShutterEvent => updateShutterState(evt)
    case SnapshotOffer(_, snapshot: ShutterState) => shutterState = snapshot
  }

  override def receiveCommand: Receive = {

    case moveShutterCommand: MoveShutter =>
      persist(ShutterMoved(moveShutterCommand.position)) { event =>
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

  def updateShutterState(event: ShutterEvent): Unit = {
    log.info(s"Updating state from event: $event")
    shutterState = ShutterState(shutterState.connectorType, event.newPosition)
  }
}
