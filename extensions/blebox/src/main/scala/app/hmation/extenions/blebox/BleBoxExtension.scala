package app.hmation.extenions.blebox

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem, ExtensionId, ExtensionIdProvider}
import akka.http.scaladsl.Http
import app.hmation.extenions.blebox.device.BleBoxShutter
import app.hmation.extension.api.ExtensionRegistryApi.Register
import app.hmation.extension.api.{HmationExtension, HmationExtensionId}

object BleBoxExtension
  extends HmationExtensionId[BleBoxExtension] {

  override def lookup = BleBoxExtension

  override def createExtension(system: ExtendedActorSystem) = new BleBoxExtension(system)

  override def get(system: ActorSystem): BleBoxExtension = super.get(system)
}

class BleBoxExtension(val actorSystem: ActorSystem)
  extends HmationExtension {

  override def configure(connectorRegistry: ActorRef) {

    // create and register worker actors
    val shutter: ActorRef = actorSystem.actorOf(BleBoxShutter.props(Http(actorSystem)), "blebox-shutter-connector")

    connectorRegistry ! Register("blebox-shutter-connector", shutter)
  }
}
