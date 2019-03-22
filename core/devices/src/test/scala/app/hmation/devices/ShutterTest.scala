package app.hmation.devices

import akka.testkit.TestProbe
import app.hmation.core.ConnectorRegistry.Api.Lookup
import app.hmation.test.common.AkkaActorTest

class ShutterTest extends AkkaActorTest {

  "Shutter" should {

    // given
    val connectorRegistry = TestProbe()

    "lookUp connectorRegistry upon creation" in {

      // when
      system.actorOf(Shutter.props("someId", connectorRegistry.testActor))

      // then
      connectorRegistry.expectMsgClass(classOf[Lookup])
    }
  }
}
