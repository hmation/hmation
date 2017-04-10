package com.hmation.core.test.common

import akka.actor.ActorSystem
import akka.testkit.TestKit
import akka.testkit.ImplicitSender
import com.typesafe.config.ConfigFactory
import org.scalatest._

abstract class AkkaActorTest extends TestKit(ActorSystem("actor-test-system", ConfigFactory.load("test-application.conf"))) with Matchers with WordSpecLike with ImplicitSender