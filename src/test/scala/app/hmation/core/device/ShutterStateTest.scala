package app.hmation.core.device

import app.hmation.core.test.common.AkkaActorTest
import app.hmation.domain.Shutter.State.ShutterState

class ShutterStateTest extends AkkaActorTest {

  private val connectorType = "someConnectorType"

  "ShutterState" should {

    "fail if created outside of ranges <0,100>" in {

      List(-1, -17, -2003232, 1000, 101)
        .foreach { element =>
          assertThrows[IllegalArgumentException] {
            ShutterState(connectorType, element)
          }
        }
    }

    "respond as Opened when position in between (0, 100)" in {
      // when
      val state = ShutterState(connectorType, 7)

      // then
      state.isClosed shouldBe false
      state.isOpened shouldBe true
      state.isFullyOpened shouldBe false
    }

    "respond as Closed when position = 100" in {
      // when
      val state = ShutterState(connectorType, 100)

      // then
      state.isClosed shouldBe true
      state.isOpened shouldBe false
      state.isFullyOpened shouldBe false
    }

    "respond as FullyOpened and Opened when position = 0" in {
      // when
      val state = ShutterState(connectorType, 0)

      // then
      state.isClosed shouldBe false
      state.isOpened shouldBe true
      state.isFullyOpened shouldBe true
    }
  }
}
