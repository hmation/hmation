package com.hmation.domain

import akka.actor.ActorRefFactory
import akka.stream.Materializer
import akka.util.Timeout

import scala.concurrent.ExecutionContextExecutor

trait AkkaConfiguration {

  implicit val actorRefFactory: ActorRefFactory
  implicit val materializer: Materializer
  implicit val executionContext: ExecutionContextExecutor = actorRefFactory.dispatcher
  implicit val askTimeout: Timeout

}

class ImplicitAkkaConfiguration(implicit
  override val actorRefFactory: ActorRefFactory,
  override val materializer: Materializer,
  override val askTimeout: Timeout
) extends AkkaConfiguration