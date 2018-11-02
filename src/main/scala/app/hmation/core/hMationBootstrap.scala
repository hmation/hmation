package app.hmation.core

import app.hmation.blebox.BleBoxExtension
import app.hmation.domain.Home.Commands.AddDevice
import app.hmation.domain.Shutter.Commands.MoveShutter
import app.hmation.domain.{Home, Shutter}

object hMationBootstrap
  extends App
    with hMationRoutes.Default
    with IdGeneration.Default
    with Akka.Default {

  val connectorRegistry = actorSystem.actorOf(ConnectorRegistry.props, "connector-registry")

  // fixme: should be automatically done
  actorSystem.extension(BleBoxExtension).configure(connectorRegistry)

  private val id = idGenerator.nextId()
  val shutter = actorSystem.actorOf(Shutter.props(id, connectorRegistry))

  shutter ! MoveShutter(34)
  shutter ! "print"
  shutter ! MoveShutter(78)
  shutter ! "print"

  Thread.sleep(3000)

  val home = actorSystem.actorOf(Home.props("myHome"))

  home ! AddDevice("someShutter")
  home ! AddDevice("someShutter2")
  home ! AddDevice("someShutter3")
}
