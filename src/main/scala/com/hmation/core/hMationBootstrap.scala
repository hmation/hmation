package com.hmation.core

import akka.actor.ActorSystem
import com.hmation.core.device.Shutter
import com.hmation.core.device.Shutter.{CloseShutter, MoveShutter, OpenShutter}

object hMationBootstrap extends App {
  val system = ActorSystem("hMation")
  val shutter = system.actorOf(Shutter.props(), "shutter")

  shutter ! MoveShutter(34)
  shutter ! "print"
  shutter ! MoveShutter(78)
  shutter ! "print"
  shutter ! CloseShutter
  shutter ! "print"
  shutter ! OpenShutter
  shutter ! "print"
  shutter ! CloseShutter
  shutter ! "print"

  Thread.sleep(10000)
  system.terminate()
}
