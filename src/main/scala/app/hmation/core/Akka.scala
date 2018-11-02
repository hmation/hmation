package app.hmation.core

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration._

object Akka {

  trait Provides {
    implicit def actorSystem: ActorSystem
    implicit def actorMaterializer: Materializer
    implicit val executionContext: ExecutionContextExecutor
    implicit val askTimeout: Timeout
  }

  trait Default extends Provides {
    implicit def actorSystem: ActorSystem = ActorSystem("hMation")
    implicit def actorMaterializer: ActorMaterializer = ActorMaterializer()
    override implicit val askTimeout: Timeout = 3 seconds
    implicit val executionContext: ExecutionContextExecutor = actorSystem.dispatcher
  }
}
