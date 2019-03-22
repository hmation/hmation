package app.hmation.infrastructure.rest.shutter

import akka.actor.{ActorRef, ActorSystem}
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.pattern.ask
import app.hmation.devices.Shutter.Commands.GetStatus
import app.hmation.devices.ShutterService

trait ShutterRoutes extends ShutterJsonSupport
  with ShutterService {

  implicit def actorSystem: ActorSystem
  implicit val connectorRegistry: ActorRef

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
