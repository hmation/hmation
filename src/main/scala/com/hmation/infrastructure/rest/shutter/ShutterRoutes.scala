package com.hmation.infrastructure.rest.shutter

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives.{concat, pathEnd, pathPrefix, _}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._

trait ShutterRoutes extends ShutterJsonSupport {

  implicit def actorSystem: ActorSystem

  lazy val log = Logging(actorSystem, classOf[ShutterRoutes])

  implicit lazy val timeout = Timeout(5 seconds)

  implicit val ec: ExecutionContext = ExecutionContext.global

  lazy val shutterRoutes: Route =
    pathPrefix("shutters") {
      concat(
        pathEnd {
          concat(
            get {
              complete(StatusCodes.OK)
            }
          )
        }
      )
    }
}
