package org.scalasteward.core.vcs.azure

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final private[azure] case class BranchResponse(value: List[BranchValue])

final private[azure] case class BranchValue(name: String, objectId: String)

final private[azure] object BranchResponse {
  implicit val branchDecoder: Decoder[BranchResponse] = deriveDecoder
  implicit val valueDecoder: Decoder[BranchValue] = deriveDecoder
}
