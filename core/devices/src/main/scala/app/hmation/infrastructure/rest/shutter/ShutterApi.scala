package app.hmation.infrastructure.rest.shutter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol._
import spray.json.JsValue

final case class ShutterDto(name: String, connectorType: String, data: JsValue)
final case class ShuttersDto(shutters: Seq[ShuttersDto])

trait ShutterJsonSupport extends SprayJsonSupport {

  implicit val shutterDtoJsonFormat = jsonFormat3(ShutterDto)
}
