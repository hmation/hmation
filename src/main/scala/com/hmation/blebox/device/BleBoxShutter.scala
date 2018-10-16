package com.hmation.blebox.device

import akka.actor.{Actor, ActorLogging, Props}
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import com.hmation.core.device.ShutterAggregate.{CloseShutter, MoveShutter, OpenShutter}

object BleBoxShutter {
  def props(http: HttpExt) = Props(classOf[BleBoxShutter], http)
}

class BleBoxShutter(val http: HttpExt) extends Actor with ActorLogging {

  import akka.pattern.pipe
  import context.dispatcher

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  override def receive: Receive = {
    case moveShutterCommand: MoveShutter =>
      http
        .singleRequest(HttpRequest(uri = "http://192.168.0.213/api/shutter/state"))
        .pipeTo(self)

    case CloseShutter =>

    case OpenShutter =>

    case HttpResponse(StatusCodes.OK, headers, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info("Got response, body: " + body.utf8String)
      }
    case resp @ HttpResponse(code, _, _, _) =>
      log.warning("Request failed, response code: " + code)
      resp.discardEntityBytes()
  }
}
