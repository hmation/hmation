package com.hmation.core.device

import akka.testkit.TestActorRef
import com.hmation.core.device.Shutter.{GetStatus, ShutterStatus}
import com.hmation.core.test.common.AkkaActorTest

class ShutterTest extends AkkaActorTest {


  "Shutter" should {

    // given
    val shutter = TestActorRef.apply(Shutter.props(ShutterStatus(75)))

    "respond with status" in {

      // when
      shutter ! GetStatus

      // then
      expectMsg(ShutterStatus(75))
    }

    "on Close command change status to closed" in {

      // when
      shutter ! Shutter.Close
      shutter ! GetStatus

      // then
      expectMsg(ShutterStatus(100))
    }

    "on Open command change status to opened" in {

      // when
      shutter ! Shutter.Open
      shutter ! GetStatus

      // then
      expectMsg(ShutterStatus(0))
    }

    "on Move command change to desired position" in {

      // when
      shutter ! Shutter.MoveShutter(33)
      shutter ! GetStatus

      // then
      expectMsg(ShutterStatus(33))
    }
  }

}
