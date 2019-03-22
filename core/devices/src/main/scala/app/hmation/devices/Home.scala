package app.hmation.devices

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import app.hmation.devices.Home.Commands._
import app.hmation.devices.Home.Events._
import app.hmation.devices.Home.State._

object Home {

  def props(homeId: String) = Props(classOf[Home], homeId)

  object Commands {
    sealed trait HomeCommand
    final case class GetDevice(deviceId: String) extends HomeCommand
    final case class AddDevice(deviceId: String) extends HomeCommand
  }

  object Events {
    sealed trait HomeEvent {
      val deviceId: String
    }
    final case class DeviceAdded(deviceId: String) extends HomeEvent
  }

  object State {
    object HomeState {
      def apply(): HomeState = HomeState(Map.empty)
    }

    final case class HomeState(devices: Map[String, String]) {
      def add[E <: HomeEvent](event: E): HomeState = HomeState(devices + (event.deviceId -> event.deviceId))
    }

    final case class HomeNotFound(id: String) extends RuntimeException(s"Home not found with id $id")

    type MaybeHome[+A] = Either[HomeNotFound, A]
  }
}

class Home(homeId: String) extends PersistentActor
  with ActorLogging{

  private var state = HomeState()

  override def persistenceId: String = homeId

  override def receiveRecover: Receive = {
    case event: HomeEvent =>
      state = state.add(event)
      log.info(s"Replay event for home=[$persistenceId]")
    case SnapshotOffer(_, snapshot: HomeState) =>
      state = snapshot
      log.info(s"Use snapshot of home=[$persistenceId]")
  }

  override def receiveCommand: Receive = {

    case GetDevice(id) =>
      sender() ! state.devices(id)
      log.info(s"Get state of home=[$persistenceId]")

    case AddDevice(deviceId) =>
      persist(DeviceAdded(deviceId)) {
        event =>
          state = state.add(event)
          context.system.eventStream.publish(event)
          log.info(s"Add device to home=[$persistenceId]")
          if (lastSequenceNr != 0 && lastSequenceNr % 1000 == 0) saveSnapshot(state)
      }
  }
}