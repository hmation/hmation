package app.hmation.domain

import akka.testkit.{TestKit, TestProbe}
import app.hmation.core.ConnectorRegistry.Lookup
import app.hmation.test.common.AkkaActorTest

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