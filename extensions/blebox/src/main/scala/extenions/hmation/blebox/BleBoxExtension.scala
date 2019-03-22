package extenions.hmation.blebox

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import akka.http.scaladsl.Http
import extenions.hmation.blebox.device.BleBoxShutter

object BleBoxExtension
  extends ExtensionId[BleBoxExtension]
    with ExtensionIdProvider {

  override def lookup = BleBoxExtension

  override def createExtension(system: ExtendedActorSystem) = new BleBoxExtension(system)

  override def get(system: ActorSystem): BleBoxExtension = super.get(system)
}

class BleBoxExtension(val actorSystem: ActorSystem) extends Extension {

  def configure(connectorRegistry: ActorRef) {

    // create and register worker actors
    val shutter: ActorRef = actorSystem.actorOf(BleBoxShutter.props(Http(actorSystem)), "blebox-shutter-connector")

    connectorRegistry ! Register("blebox-shutter-connector", shutter)
  }
}
