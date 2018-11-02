package app.hmation.domain

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.util.Timeout
import app.hmation.core.Akka

import scala.concurrent.ExecutionContextExecutor

class ImplicitAkkaConfiguration(implicit
  override val actorSystem: ActorSystem,
  override val actorMaterializer: Materializer,
  override val askTimeout: Timeout,
  override val executionContext: ExecutionContextExecutor
) extends Akka.Provides