package app.hmation.infrastructure.rest.shutter

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol._
import spray.json.JsValue

final case class HomeDto(name: String, data: JsValue)

trait HomeJsonSupport extends SprayJsonSupport {

  implicit val homeDtoJsonFormat = jsonFormat2(HomeDto)
}
