package app.hmation.domain

import akka.testkit.TestActorRef
import app.hmation.domain.Switch.SwitchStatus
import app.hmation.test.common.AkkaActorTest

class SwitchTest extends AkkaActorTest {

  "Switch" should {

    // given
    val switch = TestActorRef.apply(Switch.props(SwitchStatus.ON))

    "respond with status" in {

      // when
      switch ! Switch.GetStatus

      // then
      expectMsg(SwitchStatus.ON)
    }

    "on Off command change status to OFF" in {

      // when
      switch ! Switch.TurnOn
      switch ! Switch.TurnOff
      switch ! Switch.GetStatus

      // then
      expectMsg(SwitchStatus.OFF)
    }

    "on On command change status to ON" in {

      // when
      switch ! Switch.TurnOff
      switch ! Switch.TurnOn
      switch ! Switch.GetStatus

      // then
      expectMsg(SwitchStatus.ON)
    }
  }
}
