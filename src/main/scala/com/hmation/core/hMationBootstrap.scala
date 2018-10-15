package com.hmation.core

import akka.actor.ActorSystem
import com.hmation.blebox.BleBoxExtension
import com.hmation.core.device.Shutter
import com.hmation.core.device.Shutter.{CloseShutter, MoveShutter, OpenShutter}

object hMationBootstrap extends App {
  val system = ActorSystem("hMation")
  val connectorRegistry = system.actorOf(ConnectorRegistry.props, "connector-registry")

  // fixme: should be automatically done
  system.extension(BleBoxExtension).configure(connectorRegistry)

  val shutter = system.actorOf(Shutter.props(connectorRegistry))

  shutter ! MoveShutter(34)
  shutter ! "print"
  shutter ! MoveShutter(78)
  shutter ! "print"
  shutter ! CloseShutter
  shutter ! "print"
  shutter ! OpenShutter
  shutter ! "print"
  shutter ! CloseShutter
  shutter ! "print"

  Thread.sleep(10000)
  system.terminate()
}
