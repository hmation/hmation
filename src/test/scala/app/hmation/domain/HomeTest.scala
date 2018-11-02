package app.hmation.domain

import app.hmation.domain.Home.Commands.{AddDevice, GetDevice}
import app.hmation.test.common.AkkaActorTest
import com.softwaremill.id.pretty.PrettyIdGenerator

import scala.concurrent.duration._

class HomeTest extends AkkaActorTest {

  val deviceId = "deviceId"

  "Home" should {
    val homeEntity = system.actorOf(Home.props(PrettyIdGenerator.singleNode.nextId()))

    "store a device and be able to get it out" in {

      // when
      homeEntity ! AddDevice(deviceId)

      // then
      homeEntity ! GetDevice(deviceId)
      expectMsg(10 seconds, deviceId)
    }
  }
}
