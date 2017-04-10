package com.hmation.core.device

import akka.testkit.TestActorRef
import com.hmation.core.device.Shutter.{GetStatus, ShutterStatus}
import com.hmation.core.test.common.AkkaActorTest
import org.scalatest.prop.TableDrivenPropertyChecks

class ShutterTest extends AkkaActorTest with TableDrivenPropertyChecks {


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

  "Shutter status" should {

    val invalidShutterPositions = Table("position", -1, -181, 101, Integer.MAX_VALUE)
    val validShutterPositions = Table("position", 1, 99, 17)

    "be not instantiable for values not in range <0, 100>" in {

      // when and then
      forAll(invalidShutterPositions) { position =>
        assertThrows[IllegalArgumentException] {
          ShutterStatus(position)
        }
      }
    }

    "be marked as closed when position is 100" in {

      // when
      val status = ShutterStatus(100)

      // then
      status.isClosed should be(true)
      status.isOpened should be(false)
      status.isFullyOpened should be(false)
    }

    "be marked as opened when position is between 0 and 100" in {

      forAll(validShutterPositions) { pos => ShutterStatus(pos).isOpened should be(true) }
    }

    "be marked as fully opened when position is 0" in {

      // when
      val status = ShutterStatus(0)

      // then
      status.isClosed should be(false)
      status.isOpened should be(true)
      status.isFullyOpened should be(true)
    }
  }

}
