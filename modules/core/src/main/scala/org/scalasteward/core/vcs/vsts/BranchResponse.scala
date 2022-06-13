package org.scalasteward.core.vcs.vsts

import io.circe.Decoder
import io.circe.generic.semiauto.deriveDecoder

final private[vsts] case class BranchResponse(value: List[BranchValue])

final private[vsts] case class BranchValue(name: String, objectId: String)

final private[vsts] object BranchResponse {
  implicit val branchDecoder: Decoder[BranchResponse] = deriveDecoder
  implicit val valueDecoder: Decoder[BranchValue] = deriveDecoder
}
