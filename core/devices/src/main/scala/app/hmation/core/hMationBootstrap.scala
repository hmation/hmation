package app.hmation.core

import java.util.ServiceLoader

import app.hmation.devices.Home.Commands.AddDevice
import app.hmation.devices.Shutter.Commands.MoveShutter
import app.hmation.devices.{Home, Shutter}
import app.hmation.extension.api.{HmationExtension, HmationExtensionId}

object hMationBootstrap
  extends App
    with hMationRoutes.Default
    with IdGeneration.Default
    with Akka.Default {

  val connectorRegistry = actorSystem.actorOf(ConnectorRegistry.props, "connector-registry")

  ServiceLoader load classOf[HmationExtensionId[HmationExtension]] forEach {
    extension => actorSystem.extension(extension).configure(connectorRegistry)
  }

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
