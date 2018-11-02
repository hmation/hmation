package app.hmation.core

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import app.hmation.infrastructure.rest.shutter.{HomeRoutes, ShutterRoutes}
import org.slf4j.LoggerFactory

object hMationRoutes {

  val log = LoggerFactory.getLogger("hMationRoutes")

  trait Requires {
    implicit def actorSystem: ActorSystem
    implicit def actorMaterializer: Materializer

    // todo: fixme, that shouldn't be defined here as required, doesn't make too much sense
    implicit val connectorRegistry: ActorRef
  }

  trait Default
    extends Requires
      with ShutterRoutes
      with HomeRoutes {

    private val interface = "0.0.0.0"
    private val port = 8080

    Http().bindAndHandle(shutterRoutes ~ homeRoutes, interface, port)
    log.info(s"Server online at http://${interface}:${port}/")
  }
}
