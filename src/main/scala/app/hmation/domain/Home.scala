package app.hmation.domain

import akka.actor.Props
import akka.persistence.{PersistentActor, SnapshotOffer}
import app.hmation.domain.Home.Commands._
import app.hmation.domain.Home.Events._
import app.hmation.domain.Home.State._

object Home {

  def props = Props(classOf[Home])

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

class Home extends PersistentActor {

  private var state = HomeState()

  override def persistenceId: String = "home"

  override def receiveRecover: Receive = {
    case event: HomeEvent => state = state.add(event)
    case SnapshotOffer(_, snapshot: HomeState) => state = snapshot
  }

  override def receiveCommand: Receive = {

    case GetDevice(id) =>
      sender() ! state.devices(id)

    case AddDevice(deviceId) =>
      persist(DeviceAdded(deviceId)) {
        event =>
          state = state.add(event)
          context.system.eventStream.publish(event)
          if (lastSequenceNr != 0 && lastSequenceNr % 1000 == 0) saveSnapshot(state)
      }
  }
}