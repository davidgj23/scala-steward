package org.scalasteward.core.vcs.azure

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

private[azure] case class CreateThreadResponse(id: Int)

private[azure] object CreateThreadResponse {
  implicit val decoder: Decoder[CreateThreadResponse] = deriveDecoder
}
