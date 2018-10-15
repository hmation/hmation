package com.hmation.core

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.hmation.blebox.BleBoxExtension
import com.hmation.core.device.Shutter
import com.hmation.core.device.Shutter.{CloseShutter, MoveShutter, OpenShutter}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object hMationBootstrap
  extends App
    with hMationBootstrapRoutes.Default {

  implicit def actorSystem: ActorSystem = ActorSystem("hMation")
  implicit def actorMaterializer: ActorMaterializer = ActorMaterializer()

  val connectorRegistry = actorSystem.actorOf(ConnectorRegistry.props, "connector-registry")

  // fixme: should be automatically done
  actorSystem.extension(BleBoxExtension).configure(connectorRegistry)

  val shutter = actorSystem.actorOf(Shutter.props(connectorRegistry))

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

  Await.result(actorSystem.whenTerminated, Duration.Inf)
}
