package org.scalasteward.core.vcs.vsts

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.scalasteward.core.git.Branch

private[vsts] case class Reviewer(id: String)
private[vsts] case class CreatePullRequestRequest(sourceRefName: Branch,
                                                  targetRefName: Branch,
                                                  title: String,
                                                  description: String,
                                                  reviewers: List[Reviewer],
                                                 )

private[vsts] object CreatePullRequestRequest {
  implicit val createPullRequestEncoder: Encoder[CreatePullRequestRequest] = deriveEncoder
  implicit val reviewerEncoder: Encoder[Reviewer] = deriveEncoder
}

