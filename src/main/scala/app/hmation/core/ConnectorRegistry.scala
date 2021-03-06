package app.hmation.core

import akka.actor.{Actor, ActorRef, DeadLetter, Props}
import app.hmation.core.ConnectorRegistry.Api._

object ConnectorRegistry {

  def props = Props(classOf[ConnectorRegistry])

  object Api {
    case class Register(name: String, actor: ActorRef)
    case class Unregister(name: String)
    case class Lookup(name: String)
  }

}

class ConnectorRegistry extends Actor {

  private var actors: Map[String, ActorRef] = Map.empty

  override def receive: Receive = {
    case Register(actorName, actor) => actors = actors + (actorName -> actor)
    case Unregister(actorName) => actors = actors - actorName
    case Lookup(actorName) => sender() ! actors.getOrElse(actorName, DeadLetter)
  }
}
