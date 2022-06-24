package org.scalasteward.core.vcs.azure

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

private[azure] case class UpdatePullRequestRequest(status: Int)

private[azure] object UpdatePullRequestRequest {
  def closePullRequestRequest = UpdatePullRequestRequest(2)
  implicit val updatePullRequestEncoder: Encoder[UpdatePullRequestRequest] = deriveEncoder
}
