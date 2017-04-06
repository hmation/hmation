package com.hmation.core

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.stream.{ActorMaterializer, Materializer}
import com.hmation.core.device.{Shutter, Switch}
import com.hmation.core.device.Shutter._
import com.hmation.core.device.Switch.{SwitchStatus, TurnOff, TurnOn}
import spray.json.DefaultJsonProtocol

import scala.concurrent.ExecutionContextExecutor

object hMationBootstrap extends App with Service {
  override implicit val system = ActorSystem()
  override implicit val executor = system.dispatcher
  override implicit val materializer = ActorMaterializer()

  override val logger = Logging(system, getClass)

  Http().bindAndHandle(routes, "0.0.0.0", 8080)
}

trait Protocols extends DefaultJsonProtocol {}

trait Service extends Protocols {

  implicit val system: ActorSystem
  implicit def executor: ExecutionContextExecutor
  implicit val materializer: Materializer

  val logger: LoggingAdapter

  val routes = {

    logRequestResult("hMation") {
      (get & pathPrefix("shutter")) {
        complete {

          val shutter = system.actorOf(Shutter.props(ShutterStatus(17)))
          val switch = system.actorOf(Switch.props(SwitchStatus.OFF))

          shutter ! Shutter.GetStatus
          shutter ! Close
          shutter ! Shutter.GetStatus
          shutter ! Open
          shutter ! Shutter.GetStatus
          shutter ! MoveShutter(72)
          shutter ! Shutter.GetStatus

          switch ! Switch.GetStatus
          switch ! TurnOn
          switch ! Switch.GetStatus
          switch ! TurnOff
          switch ! Switch.GetStatus

          OK
        }
      }
    }
  }
}
