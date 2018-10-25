package app.hmation.core.device

import akka.testkit.{TestKit, TestProbe}
import app.hmation.core.ConnectorRegistry.Lookup
import app.hmation.core.test.common.AkkaActorTest
import app.hmation.domain.Shutter

class ShutterTest extends AkkaActorTest {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

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
