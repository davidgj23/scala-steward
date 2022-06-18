package org.scalasteward.core.vcs.vsts

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

private[vsts] case class CreateThreadResponse(id: Int)

private[vsts] object CreateThreadResponse {
  implicit val decoder: Decoder[CreateThreadResponse] = deriveDecoder
}
