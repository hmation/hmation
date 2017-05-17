package com.hmation.core

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer

object hMationBootstrap extends App {
  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  implicit val logger = Logging(system, getClass)

}
