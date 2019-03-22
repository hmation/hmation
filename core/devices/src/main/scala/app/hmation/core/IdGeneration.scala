package app.hmation.core

import com.softwaremill.id.pretty.{PrettyIdGenerator, StringIdGenerator}

object IdGeneration {

  trait Default {
    val idGenerator: StringIdGenerator = PrettyIdGenerator.singleNode
  }

}
