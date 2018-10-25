package app.hmation.domain

import akka.actor.Props
import app.hmation.domain.Home.State.MaybeHome

import scala.concurrent.Future

trait HomeService extends AkkaConfiguration {

  import akka.pattern.ask

  private val homeEntity = actorRefFactory.actorOf(Props[Home])

  def getDevice(id: String): Future[MaybeHome[String]] =
    (homeEntity ? Home.Commands.GetDevice(id)).mapTo[MaybeHome[String]]

  def addDevice(id: String): Future[String] =
    (homeEntity ? Home.Commands.AddDevice(id)).mapTo[String]
}
