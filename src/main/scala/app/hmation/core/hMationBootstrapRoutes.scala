package app.hmation.core

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import app.hmation.infrastructure.rest.shutter.ShutterRoutes

object hMationBootstrapRoutes {

  trait Requires {
    implicit def actorSystem: ActorSystem
    implicit def actorMaterializer: ActorMaterializer

    implicit val connectorRegistry: ActorRef
  }

  trait Default
    extends Requires
      with ShutterRoutes {
    Http().bindAndHandle(shutterRoutes, "localhost", 8080)
    println(s"Server online at http://localhost:8080/")
  }
}
