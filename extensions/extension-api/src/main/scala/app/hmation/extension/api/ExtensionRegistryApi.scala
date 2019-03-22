package app.hmation.extension.api

trait ExtensionRegistryApi {

  object Api {

    case class Register(name: String, actor: ActorRef)

    case class Unregister(name: String)

    case class Lookup(name: String)

  }


}
