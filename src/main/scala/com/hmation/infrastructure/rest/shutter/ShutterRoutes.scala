package com.hmation.infrastructure.rest.shutter

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import akka.util.Timeout
import com.hmation.domain.Shutter.Commands.GetStatus

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait ShutterRoutes extends ShutterJsonSupport {

  implicit def actorSystem: ActorSystem
  implicit val connectorRegistry: ActorRef

  lazy val log = Logging(actorSystem, classOf[ShutterRoutes])

  implicit lazy val timeout: Timeout = Timeout(5 seconds)

  implicit val ec: ExecutionContext = ExecutionContext.global

  lazy val shutterRoutes: Route =
    pathPrefix("api" / "v1" / "shutters") {
      path(Remaining) { shutterId =>
        get {
          complete {
            actorSystem
              .actorSelection(s"user/shutter-$shutterId")
              .resolveOne
              .map {
                _ ? GetStatus map {
                  case (id, connectorType, position) => HttpResponse(status = OK, entity = s"id=$id, type=$connectorType, position=$position")
                }
              }
          }
        }
      }
    }
}
