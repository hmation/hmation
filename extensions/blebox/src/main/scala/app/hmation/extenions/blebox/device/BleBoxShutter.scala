package app.hmation.extenions.blebox.device

import akka.actor.{ActorLogging, Props}
import akka.http.scaladsl.HttpExt
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.pattern.pipe
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import akka.util.ByteString
import app.hmation.extension.api.ShutterExtensionApi.ShutterExtension
import app.hmation.extension.api.ShutterExtensionApi.commands.{CloseShutter, MoveShutter, OpenShutter}

object BleBoxShutter {
  def props(http: HttpExt) = Props(classOf[BleBoxShutter], http)
}

class BleBoxShutter(val http: HttpExt)
  extends ShutterExtension
    with ActorLogging {

  final implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(context.system))

  import context.dispatcher

  override def receive: Receive = {
    case _: MoveShutter =>
      http
        .singleRequest(HttpRequest(uri = "http://192.168.0.213/api/shutter/state"))
        .pipeTo(self)

    case CloseShutter =>

    case OpenShutter =>

    case HttpResponse(StatusCodes.OK, _, entity, _) =>
      entity.dataBytes.runFold(ByteString(""))(_ ++ _).foreach { body =>
        log.info("Got response, body: " + body.utf8String)
      }
    case resp@HttpResponse(code, _, _, _) =>
      log.warning("Request failed, response code: " + code)
      resp.discardEntityBytes()

    case any@_ => log.error(s"$any")
  }
}
