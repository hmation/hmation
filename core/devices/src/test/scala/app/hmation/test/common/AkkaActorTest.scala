package app.hmation.test.common

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import com.typesafe.config.ConfigFactory
import org.scalatest._

abstract class AkkaActorTest
  extends TestKit(ActorSystem("actor-test-system", ConfigFactory.load("test-application.conf")))
    with Matchers
    with WordSpecLike
    with ImplicitSender
    with BeforeAndAfterAll