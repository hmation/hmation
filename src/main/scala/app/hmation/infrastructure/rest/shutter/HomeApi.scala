package app.hmation.infrastructure.rest.shutter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsValue}

final case class HomeDto(name: String, data: JsValue)

trait HomeJsonSupport extends SprayJsonSupport {

  import DefaultJsonProtocol._

  implicit val homeDtoJsonFormat = jsonFormat2(HomeDto)
}
