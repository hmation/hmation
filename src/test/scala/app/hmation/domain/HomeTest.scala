package app.hmation.domain

import akka.testkit.TestKit
import app.hmation.core.test.common.AkkaActorTest
import app.hmation.domain.Home.Commands.{AddDevice, GetDevice}

class HomeTest extends AkkaActorTest {

  val deviceId = "deviceId"

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Home" should {
    val homeEntity = system.actorOf(Home.props)

    "store a device and be able to get it out" in {

      // when
      homeEntity ! AddDevice(deviceId)

      // then
      homeEntity ! GetDevice(deviceId)
      expectMsg(deviceId)
    }
  }
}
