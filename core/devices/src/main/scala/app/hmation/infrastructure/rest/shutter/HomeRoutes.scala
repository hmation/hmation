package app.hmation.infrastructure.rest.shutter

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.StatusCodes.OK
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import app.hmation.devices.HomeService

trait HomeRoutes extends HomeJsonSupport
  with HomeService {

  implicit def actorSystem: ActorSystem

  lazy val homeRoutes: Route =
    pathPrefix("api" / "v1" / "home") {
      path(Remaining) { homeId =>
        get {
          complete {
            getDevice(homeId)
              .map {
                case a @ _ => HttpResponse(status = OK, entity = s"$a")
              }
          }
        }
      }
    }
}
