package com.hmation.core

import akka.actor.{Actor, ActorRef, DeadLetter, Props}
import com.hmation.core.ConnectorRegistry.{Lookup, Register, Unregister}

object ConnectorRegistry {

  def props = Props(classOf[ConnectorRegistry])

  // API
  case class Register(name: String, actor: ActorRef)
  case class Unregister(name: String)
  case class Lookup(name: String)

}

class ConnectorRegistry extends Actor {

  private var actors: Map[String, ActorRef] = Map.empty

  override def receive: Receive = {
    case Register(actorName, actor) => actors = actors + (actorName -> actor)
    case Unregister(actorName) => actors = actors - actorName
    case Lookup(actorName) => sender() ! actors.getOrElse(actorName, DeadLetter)
  }
}
