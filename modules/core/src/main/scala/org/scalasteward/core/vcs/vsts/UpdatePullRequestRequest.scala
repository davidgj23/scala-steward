package org.scalasteward.core.vcs.vsts

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

private[vsts] case class UpdatePullRequestRequest(status: Int)

private[vsts] object UpdatePullRequestRequest {
  def closePullRequestRequest = UpdatePullRequestRequest(2)
  implicit val updatePullRequestEncoder: Encoder[UpdatePullRequestRequest] = deriveEncoder
}
