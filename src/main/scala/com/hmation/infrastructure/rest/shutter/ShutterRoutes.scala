package com.hmation.infrastructure.rest.shutter

import akka.actor.{ActorNotFound, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.{PathMatcher, Route}
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout
import akka.pattern.ask
import com.hmation.core.device.ShutterAggregate.GetStatus

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait ShutterRoutes extends ShutterJsonSupport {

  implicit def actorSystem: ActorSystem

  lazy val log = Logging(actorSystem, classOf[ShutterRoutes])

  implicit lazy val timeout = Timeout(5 seconds)

  implicit val ec: ExecutionContext = ExecutionContext.global

  lazy val shutterRoutes: Route =
    pathPrefix("api" / "v1" / "shutters") {
      path(Remaining) { id =>
        get {
          complete {
            actorSystem
              .actorSelection(s"user/user-$id")
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
