package org.scalasteward.core.vcs.azure

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder
import org.scalasteward.core.git.Branch
import org.scalasteward.core.vcs.data.NewPullRequestData

private[azure] case class ResourceRef(id: String)
private[azure] case class Reviewer(id: String)
private[azure] case class CreatePullRequestRequest(sourceRefName: Branch,
                                                   targetRefName: Branch,
                                                   title: String,
                                                   description: String,
                                                   reviewers: List[Reviewer],
                                                   workItemRefs: List[ResourceRef]
                                                 )

private[azure] object CreatePullRequestRequest {
  def apply(data: NewPullRequestData): CreatePullRequestRequest = {
    CreatePullRequestRequest(
      Branch(s"refs/heads/${data.head}"),
      data.base.copy(name = s"refs/heads/${data.base.name}"),
      data.title,
      data.body,
      List(Reviewer("d2d6e9e7-a489-67a9-9969-0bef18f2df99")), //FIXME: Make it a config param
      data.workItems.map(id => ResourceRef(id))
    )
  }
  implicit val createPullRequestEncoder: Encoder[CreatePullRequestRequest] = deriveEncoder
  implicit val reviewerEncoder: Encoder[Reviewer] = deriveEncoder
  implicit val resourceRefEncoder: Encoder[ResourceRef] = deriveEncoder
}
