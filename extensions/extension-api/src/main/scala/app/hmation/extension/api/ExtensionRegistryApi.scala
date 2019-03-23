package app.hmation.extension.api

import akka.actor.{ActorRef, Extension, ExtensionId, ExtensionIdProvider}

trait HmationExtension
  extends Extension {
  def configure(connectorRegistry: ActorRef)
}

trait HmationExtensionId[EXTENSION <: HmationExtension]
  extends ExtensionId[EXTENSION]
    with ExtensionIdProvider

object ExtensionRegistryApi {

  case class Register(name: String, actor: ActorRef)

  case class Unregister(name: String)

  case class Lookup(name: String)

}
