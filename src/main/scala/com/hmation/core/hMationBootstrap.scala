package com.hmation.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.hmation.blebox.BleBoxExtension
import com.hmation.domain.Shutter
import com.hmation.domain.Shutter.Commands._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object hMationBootstrap
  extends App
    with hMationBootstrapRoutes.Default
    with IdGeneration.Default {

  implicit def actorSystem: ActorSystem = ActorSystem("hMation")
  implicit def actorMaterializer: ActorMaterializer = ActorMaterializer()

  val connectorRegistry = actorSystem.actorOf(ConnectorRegistry.props, "connector-registry")

  // fixme: should be automatically done
  actorSystem.extension(BleBoxExtension).configure(connectorRegistry)

  private val id = idGenerator.nextId()
  val shutter = actorSystem.actorOf(Shutter.props(id, connectorRegistry), s"shutter-$id")

  shutter ! MoveShutter(34)
  shutter ! "print"
  shutter ! MoveShutter(78)
  shutter ! "print"

  Await.result(actorSystem.whenTerminated, Duration.Inf)
}
