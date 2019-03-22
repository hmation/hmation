package app.hmation.core

import akka.actor.DeadLetter
import akka.testkit.TestProbe
import app.hmation.core.ConnectorRegistry.Api.{Lookup, Register, Unregister}
import app.hmation.test.common.AkkaActorTest

class ConnectorRegistryTest extends AkkaActorTest {

  "ConnectorRegistry" should {
    val registry = system.actorOf(ConnectorRegistry.props)
    val connector = TestProbe()

    "register a connector" in {
      // when
      registry ! Register("reg", connector.ref)

      // then
      registry ! Lookup("reg")
      expectMsg(connector.ref)
    }

    "unregister a connector" in {
      // when
      registry ! Register("unreg", connector.ref)
      registry ! Unregister("unreg")

      // then
      registry ! Lookup("unreg")
      expectMsg(DeadLetter)
    }
  }
}
