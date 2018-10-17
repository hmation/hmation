package com.hmation.core.device

import akka.testkit.{TestKit, TestProbe}
import com.hmation.core.ConnectorRegistry.Lookup
import com.hmation.core.test.common.AkkaActorTest

class ShutterAggregateTest extends AkkaActorTest {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Shutter" should {

    // given
    val connectorRegistry = TestProbe()

    "lookUp connectorRegistry upon creation" in {

      // when
      system.actorOf(ShutterAggregate.props("someId", connectorRegistry.testActor))

      // then
      connectorRegistry.expectMsgClass(classOf[Lookup])
    }
  }
}