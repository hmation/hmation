package com.hmation.core.device

import akka.testkit.TestActorRef
import com.hmation.core.test.common.AkkaActorTest
import com.hmation.domain.Switch
import com.hmation.domain.Switch.SwitchStatus

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
