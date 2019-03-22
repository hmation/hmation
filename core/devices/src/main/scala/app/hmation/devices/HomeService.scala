package app.hmation.devices

import akka.pattern.ask
import app.hmation.core.Akka
import app.hmation.devices.Home.State.MaybeHome

import scala.concurrent.Future

trait HomeService extends Akka.Provides {

  private def homeEntity(homeId: String) = actorSystem.actorOf(Home.props(homeId))

  def getDevice(homeId: String): Future[MaybeHome[String]] =
    (homeEntity(homeId) ? Home.Commands.GetDevice(homeId)).mapTo[MaybeHome[String]]

  def addDevice(homeId: String): Future[String] =
    (homeEntity(homeId) ? Home.Commands.AddDevice(homeId)).mapTo[String]
}
