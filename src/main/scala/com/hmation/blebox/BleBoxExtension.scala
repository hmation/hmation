package com.hmation.blebox

import akka.actor.{ActorRef, ActorSystem, ExtendedActorSystem, Extension, ExtensionId, ExtensionIdProvider}
import com.hmation.blebox.device.BleBoxShutter
import com.hmation.core.ConnectorRegistry.Register

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
    val shutter: ActorRef = actorSystem.actorOf(BleBoxShutter.props, "blebox-shutter-connector")

    connectorRegistry ! Register("blebox-shutter-connector", shutter)
  }

}
